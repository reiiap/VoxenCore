# VoxenCore

**VoxenCore** adalah framework plugin berbasis Paper yang dikembangkan secara khusus untuk kebutuhan server **VoxenSMP**. Plugin ini dirancang sebagai fondasi utama seluruh sistem server dengan arsitektur modular berbasis **Java 21**, sehingga seluruh fitur inti dapat dikelola dalam satu ekosistem yang terstruktur, cepat, dan mudah dikembangkan.

Framework ini menangani berbagai layanan inti seperti cache, scheduler, threading, data pemain, packet abstraction, GUI, menu, placeholder, ekonomi, hologram, NPC, scoreboard, TAB, konfigurasi, hingga berbagai integrasi plugin lainnya.

> ⚠️ **Catatan**
>
> Repository ini merupakan **proyek resmi milik VoxenSMP** dan dikembangkan secara internal. Seluruh source code, desain arsitektur, serta implementasi yang terdapat di dalam repository ini merupakan hak milik pengembang VoxenSMP.

---

# Persyaratan

- Java 21
- Paper 1.21.x
- Gradle 8.x

---

# Arsitektur

Repository ini menggunakan struktur **Gradle Kotlin DSL Multi-Module**.

Setiap module memiliki tanggung jawab (bounded context) masing-masing dan hanya berkomunikasi melalui kontrak yang tersedia pada module `voxencore-api`, sehingga dependency antar module tetap bersih dan mudah dipelihara.

## Module Inti

- `voxencore-api`
  - Kontrak Service
  - Module API
  - Event Bus
  - Registry System

- `voxencore-core`
  - Entry Point Plugin
  - Bootstrap Framework
  - Lifecycle Management

- `voxencore-cache`
  - Cache Abstraction
  - Memory Cache

- `voxencore-scheduler`
  - Scheduler Terpusat
  - Task Executor

- `voxencore-thread`
  - Thread Pool khusus untuk setiap subsystem

Selain module utama, repository ini juga telah dipisahkan menjadi berbagai bounded context seperti:

- Database
- Packet
- Player
- GUI
- Item
- Hologram
- NPC
- Menu
- Placeholder
- Economy
- Resource Pack
- Contract
- Dungeon
- Fishing
- Announcement
- Scoreboard
- TAB
- Configuration
- Utility

Pendekatan modular ini mempermudah pengembangan fitur baru tanpa memengaruhi subsystem lainnya.

---

# Pengembangan

Untuk melakukan build seluruh module gunakan perintah berikut:

```bash
gradle build
```

Seluruh kode ditulis menggunakan **Java 21** dan API publik didokumentasikan menggunakan **Javadoc** agar memudahkan proses pengembangan di masa mendatang.

---

# Strategi Dependency Paper API

Module `voxencore-core` dikompilasi menggunakan **Paper Bootstrap Stub** lokal (`voxencore-paper-stub`).

Pendekatan ini memungkinkan project tetap dapat dibangun pada lingkungan CI/CD yang memiliki pembatasan akses ke repository Maven eksternal.

Stub tersebut **tidak akan disertakan** pada file plugin hasil build dan hanya digunakan sebagai representasi bootstrap API Paper selama proses pengembangan.

Ketika implementasi subsystem selesai, dependency akan menggunakan **Paper API resmi**.

---

# Phase 2 — Core Infrastructure

Tahap kedua pengembangan menambahkan berbagai layanan dasar framework, di antaranya:

### voxencore-config

- Async Configuration Loader
- Hot Reload
- Validasi Konfigurasi
- Schema Migration

### voxencore-database

- JDBC Abstraction
- SQLite
- MySQL
- MariaDB
- PostgreSQL
- Integrasi HikariCP
- Repository Pattern
- Entity Mapping
- Database Migration
- Transaction Management

### voxencore-core

- Logging System
- Metrics
- Module Loader
- Hook Manager
- Lifecycle Management

---

# Phase 3 — Player Platform

Tahap ketiga menghadirkan platform pengelolaan pemain melalui module `voxencore-player`.

Fitur yang disediakan meliputi:

- Immutable Player Profile
- Session Management
- Expiring Player Cache
- Cooldown System
- Async Repository & Service
- Dirty Session Batch Saving
- Auto Save saat Disconnect
- Crash Recovery Checkpoint
- Framework Player Events

Platform ini dirancang agar pengelolaan data pemain tetap aman, cepat, dan efisien meskipun server memiliki jumlah pemain yang tinggi.

---

# Lisensi

Copyright © VoxenSMP. Seluruh hak cipta dilindungi.

Repository ini merupakan proyek internal yang dikembangkan khusus untuk kebutuhan server **VoxenSMP**.

Source code **tidak diperbolehkan** untuk:

- Menyalin sebagian maupun seluruh kode tanpa izin.
- Menjual ulang source code.
- Mendistribusikan ulang repository.
- Mengklaim karya ini sebagai milik pribadi.
- Menggunakan source code untuk proyek komersial tanpa persetujuan tertulis dari pengembang VoxenSMP.

Penggunaan, modifikasi, maupun distribusi di luar izin resmi dari pengembang VoxenSMP dapat dikenakan tindakan sesuai ketentuan lisensi yang berlaku.

---

**Developed with ❤️ by VoxenSMP Development Team**
