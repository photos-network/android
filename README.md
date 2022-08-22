# Photos.network

[![License](https://img.shields.io/github/license/photos-network/android)](./LICENSE.md)
[![GitHub contributors](https://img.shields.io/github/contributors/photos-network/android?color=success)](https://github.com/photos-network/android/graphs/contributors)
[![Discord](https://img.shields.io/discord/793235453871390720)](https://discord.gg/dGFDpmWp46)
[![Continuous Delivery Pipeline](https://github.com/photos-network/android/actions/workflows/continuous-delivery-pipeline.yml/badge.svg)](https://github.com/photos-network/android/actions/workflows/continuous-delivery-pipeline.yml)


[Photos.network](https://photos.network) is an open source project for self hosted photo management.
Its core features are:

- Share photos with friends, family or public
- Filter / Search photos by attributes like location or date
- Group photos by objects like people of objects

## App workflow
To connect the app with a [core instance](https://github.com/photos-network/core), the user needs to authenticate itself via oauth.
After adding the **Host** and **Client ID** into the app, a browser window will be opened to enter the users credentials.
```mermaid
sequenceDiagram
    participant U as User
    participant A as App
    participant C as Core Instance
    U->>A: Enter Hostname
    A->>C: Init a connection to the entered host to validate
    U->>A: Enter Client ID
    A->>C: Validate the entered client ID
    C->>A: Request user credentials
    U->>A: Enter username and password
    A->>C: Authenticate the user by the entered credentials
    C->>A: Return a token Pair (access & refresh)
```

The synchronisation of photos with a core instance is done in multiple steps:
```mermaid
  graph TB
    subgraph Sync
      worker1[SyncLocalPhotosWorker]-- Query images from <br/>Androids local MediaStore --> repo1(Photos repository)
    end
    
    subgraph Upload
      worker2[UploadPhotosWorker]-- Uploads non-synced photos --> core((Core instance))
    end
```

## Gitflow
- *main:* contains production code
- *development:* latest changes that will be included in the next release
- *feature/:* each feature separated until it is done and merged back to development
- *release/:* signifies an upcoming release and will be merged into main
- *hotfix/:* urgent changes to be merged into release and development

## Continuous Delivery Pipeline
The whole pipeline is automated into Github workflows.

- Code checks to enforce code quality & style
- Tests to ensure a stable and release-ready codebase
- Deployment into a preview environment
- Release to production

## Contribution
The help of the community is essential for projects like this. Users have different requirements and perspectives how their instances should work.

### Getting Started

Create a Feature request with a short but understandable description what the feature should look like and how the user can use it.

### Making Changes

* Create a `/feature/<topic>` branch from where you want to base your work.
  * This is usually the `development` branch.
  * Only target `release` branches if you are certain your fix must be on that branch.
* Make commits of logical and atomic units.
* Check for unnecessary whitespace with `git diff --check` before committing.
* Make sure your commit messages are in the proper format. Start the first
  line of the commit with the issue number in parentheses.
* run tests and code quality checks locally ```./gradlew detekt lint testDebugUnitTest connectedAndroidTest```
