# JDK versions

The default and other supported JDK versions are listed in the README, section "Tech stack".

- Releases for the default JDK version have plain version numbers, for example `x.y.z`. They are built from the branches `dev` and `main`.
- Releases for another supported JDK version have the same version number with the suffix `-jdkNN`, for example `x.y.z-jdk21` for JDK 21. They are built from the branch `dev-jdkNN`, for example `dev-jdk21`, from the same code, adapted where that JDK version needs it.
- Each release is published as the Docker image `muneer2ishtech/ishtech-springboot-books-app` on Docker Hub, tagged with the version, for example `x.y.z` or `x.y.z-jdk21`.
- Use the image tag that matches your JDK version.
