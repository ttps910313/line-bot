# Deploy LINE Bot Java Examples on Render

This repo can be used to deploy Java examples in the [line-bot-sdk-java](https://github.com/line/line-bot-sdk-java) on [Render](https://render.com/). Render doesn't natively support Java. A Dockerfile with Gradle and JDK image is used to deploy the Java samples.

## Prerequisites
Make sure you have the following:
- A dedicated [Messaging API channel](https://developers.line.biz/en/docs/messaging-api/getting-started/) for your bot.
- A [Render account](https://dashboard.render.com/register) that doesn't require credit card to sign up.

## Deployment
1. Fork this repo.
   > Besides `git clone` to clone this repo, you also need to execute `git submodule  update --init` to clone the `line-bot-sdk-java` submodule.
3. Update `render.yaml` to comment/uncomment the value of the `SAMPLE_FOLDER` environment variable to specify which LINE bot examples you want to deploy.
4. Cieck to deploy
   
   [![Deploy to Render](http://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy)

4. You will be prompted to input LINE channel secret and [access token](https://developers.line.biz/en/docs/messaging-api/channel-access-tokens/). You can find them on the [LINE Developers Console](https://developers.line.biz/console/). Channel secret is on the channel's `Basic settings` tab. Channel access token is on the channel's `Messaging API` tab.
5. Once the bot servcie is live, find the service `onrender` URL (e.g., `https://line-bot-java-<something unique>.onrender.com`) on the Dashboard. Append `/callback` to the service URL to build the webhook URL (e.g., `https://line-bot-java-<something unique>.onrender.com/callback`). Paste the webhook URL to the `Webhook settings` section on the LINE channel's `Messaging API` tab on the [LINE Developers Console](https://developers.line.biz/console/). Also enable `Use webhook` on the same section.
6. Add the LINE Official Account associated with your bot as a friend on LINE by scanning the QR code on the `Messaging API` tab of your channel settings on the [LINE Developers Console](https://developers.line.biz/console/).
7. That's it. Send your LINE Official Account a text message on LINE and confirm that it responds with the same message.

## Notes
- The `build.gradle` in the `line-bot-sdk-java` uses the maven plugin which has been deprecated in Gradle 7.0. The Gradle in the Dockerfile is 6.x.
