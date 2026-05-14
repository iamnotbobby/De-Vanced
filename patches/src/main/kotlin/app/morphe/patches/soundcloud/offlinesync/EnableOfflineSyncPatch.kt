/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/soundcloud/offlinesync/EnableOfflineSyncPatch.kt
 */
package app.morphe.patches.soundcloud.offlinesync

import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.FeatureConstructorFingerprint

@Suppress("unused")
val enableOfflineSync = bytecodePatch(
    name = "Enable offline sync",
) {
    compatibleWith(AppCompatibilities.SOUNDCLOUD)

    execute {
        // Force-enable the offline_sync feature flag by writing directly to Feature.enabled.
        FeatureConstructorFingerprint.method.addInstructions(
            2,
            """
                const/4 p2, 0x1
                iput-boolean p2, p0, Lcom/soundcloud/android/configuration/plans/Feature;->enabled:Z
            """.trimIndent()
        )
    }
}

