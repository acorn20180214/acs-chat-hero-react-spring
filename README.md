---
page_type: sample
languages:
- React/Redux
- Spring boot/Java
products:
- azure
- azure-communication-services
---

# Group Chat Sample

This project is inspired by the sample project implemented by [React + ASP.NET Core](https://github.com/Azure-Samples/communication-services-web-chat-hero)

This is a sample application to show how the Chat Web Java SDK can be used to build a single threaded chat experience.
The client-side application is a React based user interface which uses Redux for handling complex state while leveraging Microsoft Fluent UI.
Powering this front-end is a Spring boot application powered by Java to connect this application with Azure Communication Services.

🤞 A live demo site is hosted on azure app service at:  https://acs-chat-demo-react-spring.azurewebsites.net/

## ❤️Feedback

We appreciate your feedback and energy helping us improve our services. [Please let us know if you are satisfied with ACS through this survey](https://microsoft.qualtrics.com/jfe/form/SV_5dtYL81xwHnUVue). 

## Prerequisites

- Create an Azure account with an active subscription. For details, see [Create an account for free](https://azure.microsoft.com/free/?WT.mc_id=A261C142F).
- [Node.js (12.18.4 and above)](https://nodejs.org/en/download/)
- [Spring 2.5.5](https://spring.io/projects/spring-boot)
- [Java 8](https://www.oracle.com/java/technologies/java8.html)
- Create an Azure Communication Services resource. For details, see [Create an Azure Communication Resource](https://docs.microsoft.com/en-us/azure/communication-services/quickstarts/create-communication-resource). You'll need to record your resource **connection string** for this project.

## Code structure

- src/main/frontend: frontend client implemented by React
  - src/main/frontend/src
    - src/main/frontend/src/Components : React components to help build the client app chat experience
    - src/main/frontend/src/Containers : Connects the redux functionality to the React components
    - src/main/frontend/src/Core : Containers a redux wrapper around the Chat SDK
    - src/main/frontend/src/index.js : Entry point for the client app
- src/main/java: backend server powered by Spring Boot
  - src/main/java/com/microsoft/acs/chat/controller/ChatController.java: Server app core logic for client app to get a token to use with the Azure Communication Services Web Chat SDK
  - src/main/java/com/microsoft/acs/chat/ChatHeroApplication.java : Server app startup logic
  - pom.xml: the XML file that contains information about the project and configuration details used by Maven to build the frontend react app and the backend spring boot app into one binary file.

## Before running the sample for the first time

1. Open an instance of PowerShell, Windows Terminal, Command Prompt or equivalent and navigate to the directory that you'd like to clone the sample to.
2. `git clone https://github.com/acorn20180214/acs-chat-hero-react-spring.git`
3. Get the `Connection String` from the Azure portal. For more information on connection strings, see [Create an Azure Communication Resources](https://docs.microsoft.com/en-us/azure/communication-services/quickstarts/create-communication-resource)
5. Once you get the `Connection String`, Add the connection string to the **src/main/resources/application.properties** file. Input your connection string in the variable: `app.connectionstring`.

## Local run

1. Go to the project folder,  compile and package using Maven:  `mvn clean package`  which will trigger react build (including node/yarn installation) and java application build.

![image](https://user-images.githubusercontent.com/50332346/135975238-76d49a95-be58-45b8-8f0a-eb2dfb175047.png)

2. Run the project using `java -jar ./target/springreact-1.0.0.jar`. The browser will open at localhost:8080.

![image](https://user-images.githubusercontent.com/50332346/135975468-c15486ad-2520-494c-ac72-91a7602ee277.png)


### Troubleshooting

1. Solution doesn\'t build, it throws errors during NPM installation/build

    Clean/rebuild the Java solution

## Publish to Azure

1. Right click project and select Publish
2. Create a new publish profile and select your app name, Azure subscription, resource group and etc.

## Building off of the sample

If you would like to build off of this sample to add chat capabilities to your own awesome application, keep a few things in mind:

- The sample serves a Single Page Application. This has a few implications.
  - By default, the served app cannot be embedded in another frame (e.g. as a web widget).
  - By default, the backend disables Cross-Origin Resource Sharing (CORS). If you'd like to serve the backend APIs from a different domain than the static content, you must enable (restricted) CORS. This can be done by configuring a middleware in the backend in ./Chat/Startup.cs, or by configuring your server framework to modify HTTP response headers.

## Additional Reading

- [Azure Communication Chat SDK](https://docs.microsoft.com/en-us/azure/communication-services/concepts/chat/sdk-features) - To learn more about the chat web sdk
- [Redux](https://redux.js.org/) - Client-side state management
- [FluentUI](https://developer.microsoft.com/en-us/fluentui#/) - Microsoft powered UI library
- [React](https://reactjs.org/) - Library for building user interfaces
