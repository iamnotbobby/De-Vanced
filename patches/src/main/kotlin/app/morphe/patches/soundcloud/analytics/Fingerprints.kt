/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/soundcloud/analytics/Fingerprints.kt
 */
package app.morphe.patches.soundcloud.analytics

import app.morphe.patcher.Fingerprint

internal object CreateTrackingApiFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/analytics/DefaultTrackingApiFactory;",
    strings = listOf("boogaloo"),
)

