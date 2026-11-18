# Gboard Quick Trad/Simp — diagnostic milestone

Target: Gboard (`com.google.android.inputmethod.latin`), Android 17 / ARM64 device, LSPosed 2.x.

This repository is a **diagnostic milestone**, not yet the functional toolbar toggle. It uses the modern LibXposed API 102. It loads only in Gboard and records runtime members for known access-point and Chinese IME classes. It intentionally does not inject a toolbar item or alter input behavior until the real provider contract and script-switch operation are verified.

## APK evidence from the supplied Gboard 18.5.10 APK

DEX inspection found these class descriptors:

- `com.google.android.libraries.inputmethod.accesspoint.widget.AccessPointsBar`
- `com.google.android.libraries.inputmethod.accesspoint.widget.AccessPointsPanel`
- `com.google.android.libraries.inputmethod.accesspoint.widget.AccessPointsPanelV2`
- `com.google.android.libraries.inputmethod.accesspoint.impl.AccessPointKeyboard`
- `com.google.android.libraries.inputmethod.accesspoint.impl.AccessPointsPanelMotionEventHandler`
- `com.google.android.apps.inputmethod.libs.chinese.ime.AsyncChineseProcessorBasedIme`

The DEX string pool also contains `SIMPLIFIED_CHINESE`, `TRADITIONAL_CHINESE`, `getScript`, `setScript`, `needScriptConversion`, and access-point customization strings including `ACCESS_POINT_EDIT_BUTTON_CLICKED` and `pref_key_access_points_showing_order`.

These are discovery clues, not proof that a public or directly callable toggle API exists. The next step is to capture the runtime member dump, then map the provider/definition registration and the exact Chinese script state transition.

## Build with GitHub Actions (no Android Studio required)

1. Create a new **public or private GitHub repository** and upload/push this folder's contents to its root.
2. Open the repository's **Actions** tab.
3. Select **Build LSPosed module** and choose **Run workflow**, or push a commit to trigger it automatically.
4. Wait for the workflow to finish successfully.
5. Open the completed run and download the `GboardQuickTradSimp-debug` artifact. It contains the debug APK.

The workflow provisions JDK 17, Android SDK Platform 37, platform-tools, and Gradle 8.13 on GitHub's hosted runner. It uses `android-actions/setup-android@v4` and explicitly requests only `platform-tools`; it does not request the obsolete SDK package named `tools` (which now causes `Failed to find package 'tools'`). No local Android Studio installation or local Android SDK is needed.

## Install and collect diagnostics

1. Download and extract the Actions artifact, then install the APK on the phone.
2. Enable the module in LSPosed and keep its scope limited to Gboard.
3. Force-stop Gboard, then open it and bring up the keyboard.
4. Export the LSPosed log and filter for `GboardQuickTradSimp`.

Do not use this diagnostic build as a feature release. It only inspects class metadata and makes no changes to Gboard.
