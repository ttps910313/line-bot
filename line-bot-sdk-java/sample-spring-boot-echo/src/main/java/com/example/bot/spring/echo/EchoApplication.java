/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import com.example.bot.spring.echo.domain.Money;
import com.example.bot.spring.echo.repository.MoneyRepository;
import com.example.bot.spring.echo.service.MoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    private final Logger log = LoggerFactory.getLogger(EchoApplication.class);

    @Resource
    private MoneyRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        final String originalMessageText = event.getMessage().getText();
        if (Objects.isNull(originalMessageText)){
            return null;
        }
        if (originalMessageText.contains("!") || originalMessageText.contains("！")) {
            List<Money> users = repository.findAll();
            if(Objects.isNull(users.get(0))) {
                return new TextMessage("請初始化資料");
            }
            List<String> targetNames = users.stream()
                    .map(Money::getName)
                    .collect(Collectors.toList());

            String names = targetNames.stream()
                    .map(Pattern::quote)
                    .collect(Collectors.joining("|"));

            Pattern namePattern = Pattern.compile(
                    "^("+ names +")(?:\\s*[（(].*[）)]|洗.*)?$"
            );

            Pattern countPattern = Pattern.compile(
                    "^招財(?:衛生紙)?\\s*[:：]\\s*(\\d+)\\s*$"
            );

            Map<String, Integer> results = new LinkedHashMap<>();
            for (String name : targetNames) {
                results.put(name, 0);
            }

            String currentName = null;

            for (String rawLine : originalMessageText.split("\\R")) {
                String line = rawLine.trim();

                Matcher nameMatcher = namePattern.matcher(line);
                if (nameMatcher.matches()) {
                    String foundName = nameMatcher.group(1);
                    currentName = targetNames.contains(foundName)
                            ? foundName
                            : null;
                    continue;
                }

                if (line.startsWith("總點數")) {
                    currentName = null;
                    continue;
                }

                if (currentName == null) {
                    continue;
                }

                Matcher countMatcher = countPattern.matcher(line);
                if (countMatcher.matches()) {
                    int count = Integer.parseInt(countMatcher.group(1));
                    results.put(currentName, results.get(currentName) + count);
                }
            }

            for (Map.Entry<String, Integer> entry : results.entrySet()) {

                String name = entry.getKey();
                Integer number = entry.getValue();

                Money user = repository.findByName(name);
                if(Objects.isNull(user)) {
                    Money addUser = new Money();
                    addUser.setName(name);
                    addUser.setNumber(0);
                    repository.save(addUser);
                } else {
                    Integer oldNumber = user.getNumber();
                    user.setNumber(oldNumber+number);
                    repository.save(user);
                }
            }
            results.forEach((name, count) ->
                    System.out.println(name + " 招財數量：" + count)
            );
        }
        return new TextMessage("已完成更新");
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
