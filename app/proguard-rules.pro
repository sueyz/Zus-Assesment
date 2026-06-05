-keepclasseswithmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}

-keep @com.squareup.moshi.JsonQualifier interface *

-keepclassmembers class * extends com.squareup.moshi.JsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}

-if class *
-keepclasseswithmembers class <1> {
    @com.squareup.moshi.Json <fields>;
}

-keep @com.squareup.moshi.JsonClass class * {
    <init>(...);
}

-keepclassmembers class **$Companion {
    @com.squareup.moshi.Json <fields>;
}
