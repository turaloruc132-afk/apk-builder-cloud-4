# apk-builder-cloud — Pulsuz APK Build Serveri (GitHub Actions)

Bu repo, `HtmlToApkConverter` Android tətbiqindən gələn sorğuları qəbul edib
əsl, işlək, imzalanmış `.apk` faylı compile edən **GitHub Actions workflow**-nu
saxlayır. Compilyasiya tam GitHub-un öz serverlərində (pulsuz) baş verir.

## Necə işləyir?

1. Android tətbiqi yeni bir branch yaradır (`build-<timestamp>`).
2. O branch-a `config.json` (tətbiq adı, paket adı, icazələr, WebView ayarları),
   `www/` qovluğu (HTML/CSS/JS), `icon.png`, `splash.png` yükləyir.
3. `repository_dispatch` event-i göndərilir → `.github/workflows/build-apk.yml`
   işə düşür.
4. Workflow `scripts/inject_config.py` ilə bu məlumatları `template-app/`
   layihəsinə tətbiq edir (applicationId, versiya, icazələr, WebView davranışı,
   ikon, splash, HTML faylları).
5. `gradle assembleRelease` ilə əsl APK compile olunur.
6. APK, branch adı ilə tag olunmuş bir **GitHub Release**-ə əlavə olunur.
7. Android tətbiqi bu release-i tapıb APK-nı telefona endirir.

## Quraşdırma (bir dəfəlik)

1. Bu qovluğun bütün məzmununu öz GitHub repo-nuzun root-una yükləyin.
2. Repo → **Settings → Actions → General → Workflow permissions** →
   **Read and write permissions** seçin və yadda saxlayın.
3. Başqa heç nə etmək lazım deyil — `template-app/` artıq tam hazır Android
   layihəsidir, `scripts/inject_config.py` onu avtomatik uyğunlaşdırır.

## Fayl strukturu

```
apk-builder-cloud/
├── .github/workflows/build-apk.yml   ← Actions workflow
├── scripts/inject_config.py          ← config.json-u tətbiq edən Python script
└── template-app/                     ← generasiya olunan WebView tətbiqinin əsası
    ├── app/
    │   ├── build.gradle.kts          ← applicationId/version placeholder-ləri
    │   └── src/main/
    │       ├── AndroidManifest.xml   ← icazə/orientasiya placeholder-ləri
    │       ├── java/.../MainActivity.kt
    │       ├── java/.../GeneratedConfig.kt  ← build zamanı yenidən yazılır
    │       ├── res/...
    │       └── assets/www/index.html ← client-dən gələn HTML ilə əvəz olunur
    └── settings.gradle.kts
```

## Real Play Store imzası (opsional)

Default olaraq APK-lar Gradle-in avtomatik debug keystore-u ilə imzalanır
(sideload üçün kifayətdir). Play Store-a yükləmək üçün:

1. Öz `upload-keystore.jks` faylınızı yaradın.
2. Onu base64 formatına çevirib GitHub repo → **Settings → Secrets and
   variables → Actions** bölməsində `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`,
   `KEY_ALIAS`, `KEY_PASSWORD` kimi secret-lər əlavə edin.
3. `template-app/app/build.gradle.kts`-də `signingConfigs.getByName("debug")`
   sətrini real `release` signing config ilə əvəz edin və workflow-a keystore-u
   decode edən bir addım əlavə edin.

## Xərc

Public repo-larda GitHub Actions limitsiz pulsuzdur. Private repo-da ayda 2000
dəqiqə pulsuz limit var (bir build ~3-6 dəqiqə çəkir, yəni ayda ~300-600 build).
