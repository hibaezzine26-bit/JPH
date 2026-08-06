# File Tree: jph

**Generated:** 8/6/2026, 9:50:08 AM
**Root Path:** `c:\Users\HP\Documents\jph`

```
├── 📁 .mvn
│   └── 📁 wrapper
│       └── 📄 maven-wrapper.properties
├── 📁 frontend
│   ├── 📁 public
│   │   ├── 🖼️ favicon.svg
│   │   └── 🖼️ icons.svg
│   ├── 📁 src
│   │   ├── 📁 assets
│   │   │   ├── 🖼️ hero.png
│   │   │   ├── 🖼️ ocp-logo.png
│   │   │   ├── 🖼️ react.svg
│   │   │   └── 🖼️ vite.svg
│   │   ├── 📁 components
│   │   │   ├── 📁 layout
│   │   │   │   ├── 📄 Layout.tsx
│   │   │   │   ├── 📄 Navbar.tsx
│   │   │   │   └── 📄 Sidebar.tsx
│   │   │   ├── 📁 reporting
│   │   │   │   └── 📄 ReportingTable.tsx
│   │   │   ├── 📁 ui
│   │   │   │   ├── 📄 Alert.tsx
│   │   │   │   ├── 📄 Badge.tsx
│   │   │   │   ├── 📄 Button.tsx
│   │   │   │   ├── 📄 Card.tsx
│   │   │   │   ├── 📄 Input.tsx
│   │   │   │   ├── 📄 Loader.tsx
│   │   │   │   ├── 📄 Loading.tsx
│   │   │   │   ├── 📄 Modal.tsx
│   │   │   │   ├── 📄 Pagination.tsx
│   │   │   │   ├── 📄 SearchBar.tsx
│   │   │   │   ├── 📄 SectionHeading.tsx
│   │   │   │   ├── 📄 StatCard.tsx
│   │   │   │   ├── 📄 Table.tsx
│   │   │   │   └── 📄 Typography.tsx
│   │   │   └── 📄 ProtectedRoute.tsx
│   │   ├── 📁 context
│   │   │   └── 📄 AuthContext.tsx
│   │   ├── 📁 hooks
│   │   ├── 📁 pages
│   │   │   ├── 📁 auth
│   │   │   │   ├── 📄 LoginPage.tsx
│   │   │   │   └── 📄 ProfilePage.tsx
│   │   │   ├── 📁 dashboard
│   │   │   │   └── 📄 Dashboard.tsx
│   │   │   ├── 📁 historique
│   │   │   │   └── 📄 HistoriquePage.tsx
│   │   │   ├── 📁 import
│   │   │   │   └── 📄 ImportPage.tsx
│   │   │   ├── 📁 reporting
│   │   │   │   └── 📄 ReportingPage.tsx
│   │   │   └── 📄 NotFoundPage.tsx
│   │   ├── 📁 routes
│   │   │   ├── 📄 AppRoutes.tsx
│   │   │   └── 📄 paths.ts
│   │   ├── 📁 services
│   │   │   ├── 📄 api.ts
│   │   │   ├── 📄 authService.ts
│   │   │   ├── 📄 historiqueService.ts
│   │   │   ├── 📄 reportingService.ts
│   │   │   └── 📄 userService.ts
│   │   ├── 📁 styles
│   │   ├── 📁 types
│   │   │   ├── 📄 auth.ts
│   │   │   ├── 📄 historique.ts
│   │   │   ├── 📄 reporting.ts
│   │   │   └── 📄 user.ts
│   │   ├── 📁 utils
│   │   │   ├── 📄 apiUtils.ts
│   │   │   └── 📄 reportingColumns.ts
│   │   ├── 🎨 App.css
│   │   ├── 📄 App.tsx
│   │   ├── 🎨 index.css
│   │   └── 📄 main.tsx
│   ├── ⚙️ .env.example
│   ├── ⚙️ .gitignore
│   ├── ⚙️ .oxlintrc.json
│   ├── 📝 README.md
│   ├── 🌐 index.html
│   ├── ⚙️ package-lock.json
│   ├── ⚙️ package.json
│   ├── 📄 postcss.config.js
│   ├── ⚙️ tsconfig.app.json
│   ├── ⚙️ tsconfig.json
│   ├── ⚙️ tsconfig.node.json
│   └── 📄 vite.config.ts
├── 📁 src
│   └── 📁 main
│       ├── 📁 java
│       │   └── 📁 com
│       │       └── 📁 ocp
│       │           └── 📁 jph
│       │               ├── 📁 config
│       │               │   ├── ☕ SecurityConfig.java
│       │               │   ├── ☕ UserDataInitializer.java
│       │               │   └── 🖼️ file.png
│       │               ├── 📁 domain
│       │               │   ├── ☕ Historique.java
│       │               │   ├── ☕ ImportExcel.java
│       │               │   ├── ☕ Reporting.java
│       │               │   ├── ☕ Responsable.java
│       │               │   ├── ☕ Role.java
│       │               │   ├── ☕ Secteur.java
│       │               │   ├── ☕ Statut.java
│       │               │   ├── ☕ Udm.java
│       │               │   ├── ☕ Utilisateur.java
│       │               │   └── 🖼️ file.png
│       │               ├── 📁 dto
│       │               │   ├── ☕ ChangePasswordDto.java
│       │               │   ├── ☕ HistoriqueDto.java
│       │               │   ├── ☕ ImportExcelDto.java
│       │               │   ├── ☕ ImportResultDto.java
│       │               │   ├── ☕ ReportingDto.java
│       │               │   ├── ☕ ReportingStatisticsDto.java
│       │               │   └── ☕ UtilisateurDto.java
│       │               ├── 📁 repository
│       │               │   ├── ☕ HistoriqueRepository.java
│       │               │   ├── ☕ ImportExcelRepository.java
│       │               │   ├── ☕ ReportingRepository.java
│       │               │   └── ☕ UtilisateurRepository.java
│       │               ├── 📁 service
│       │               │   ├── ☕ HistoriqueService.java
│       │               │   ├── ☕ ImportExcelService.java
│       │               │   ├── ☕ JpaUserDetailsService.java
│       │               │   ├── ☕ ReportingService.java
│       │               │   └── ☕ UtilisateurService.java
│       │               ├── 📁 web
│       │               │   ├── 📁 exception
│       │               │   │   ├── ☕ ValidationError.java
│       │               │   │   ├── ☕ ValidationErrorResponse.java
│       │               │   │   └── ☕ ValidationExceptionHandler.java
│       │               │   ├── 📁 mapper
│       │               │   │   ├── ☕ HistoriqueMapper.java
│       │               │   │   ├── ☕ ImportExcelMapper.java
│       │               │   │   ├── ☕ ReportingMapper.java
│       │               │   │   └── ☕ UtilisateurMapper.java
│       │               │   ├── ☕ HistoriqueController.java
│       │               │   ├── ☕ ImportExcelController.java
│       │               │   ├── ☕ ReportingController.java
│       │               │   └── ☕ UtilisateurController.java
│       │               └── ☕ JphApplication.java
│       └── 📁 resources
│           └── 📄 application.properties
├── ⚙️ .gitattributes
├── ⚙️ .gitignore
├── 📝 Untitled-2.md
├── 📘 jhp.docx
├── 📄 mvnw
├── 📄 mvnw.cmd
└── ⚙️ pom.xml
```

---
*Generated by FileTree Pro Extension*