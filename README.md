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
