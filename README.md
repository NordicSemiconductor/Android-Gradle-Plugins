# Nordic Gradle Plugins

A collection of **Gradle plugins** and a **version catalog** used across all Nordic Semiconductor 
projects. 
This repository provides a unified, maintainable build setup for developing and releasing 
Nordic Android and multiplatform libraries and applications.

## Overview

The **Nordic Gradle Plugins** project consists of two main parts:

### Gradle Plugins

A set of internal plugins that standardize build configurations across Nordic projects.

- Common configuration for libraries and applications
- Integration with **Maven Central**, **Dokka**, and **Android Gradle Plugin**
- Simplified setup for consistent builds across repositories

See [PLUGINS.md](./PLUGINS.md) for details.

---

### Version Catalog

A shared **Gradle Version Catalog** defining all third-party dependencies and their versions 
in one central place.  
This ensures consistency and easy dependency upgrades across all Nordic Android projects.

See [VERSION_CATALOG.md](./VERSION_CATALOG.md) for details.

---

## Related Projects

All Nordic libraries are released with a dedicated **Version Catalog** and **BOM (Bill of Materials)**
hosted in a separate repository:

🔗 [Nordic Android Version Catalog & BOM](https://github.com/NordicSemiconductor/Android-Version-Catalog)

Use this catalog in your projects to automatically align versions of Nordic libraries.

## License

This project is licensed under the [BSD-3-Clause License](./LICENSE).