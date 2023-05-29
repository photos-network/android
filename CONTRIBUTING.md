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
* run tests and code quality checks locally ```./run_tests```


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



Each user needs to authenticate itself via [OAuth 2.0](https://datatracker.ietf.org/doc/html/rfc6749) with an [Authorization Code Grant](https://www.oauth.com/oauth2-servers/server-side-apps/authorization-code/).

After adding the **Host** and **Client ID** into the app, the app will construct the request URI
and display it in a browser window so the user can enter its credentials.
```mermaid
sequenceDiagram
    actor U as User
    participant A as App
    participant C as Core Instance
    U->>A: Enter Hostname
    A--)C: Validate entered host
    U->>A: Enter Client ID
    A--)C: Validate entered client ID
    
    alt OAuth Authorization Code Grant flow
    
      C->>U: Show credentials form (RFC 4.1.1)
      activate U
      U->>C: Send username and password
      activate C
      deactivate U
      C->>A: Respond with auth code (RFC 4.1.2)
      A->>C: Request access token (RFC 4.1.3)
      C->>A: Respond with access token (RFC 4.1.4)
      deactivate C
    end
    A->>C: Load data with access token
    C->>A: Return data
    A->>U: Display data to user

```

## Synchronization

The synchronisation of photos with a core instance is done in multiple steps:

```mermaid
flowchart LR
    store[Android media store]
    repo[(Photos repository)]
    core((Core instance))

    store --> syncWorker --> repo
    repo --> uploadWorker --> core
    core --> downloadWorker --> repo
    
    subgraph Local Sync
        syncWorker(SyncLocalPhotosWorker)
    end

    subgraph Upload
        uploadWorker[UploadWorker]
    end 

    subgraph Download
        downloadWorker[DownloadWorker]
    end
```
