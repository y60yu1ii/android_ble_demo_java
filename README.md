# An Android BLE Broadcast demo for NCKU BME Bio APP workshop (Java ver.)
- Add the following lib in your gradle file
```
dependencies {
  //Lumos ble
  implementation 'de.fishare.lumosble:lumosble:0.1.4r1'
  //MUST HAVE to inital centralmanager singlton
  implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.11'
  implementation 'nl.dionsegijn:konfetti:1.1.2'

  //Optional  ----------------------------
  //volley for internet connection
  implementation 'com.android.volley:volley:1.1.1'
  //card layout material design
  implementation 'com.android.support:design:28.0.0'
}
```
