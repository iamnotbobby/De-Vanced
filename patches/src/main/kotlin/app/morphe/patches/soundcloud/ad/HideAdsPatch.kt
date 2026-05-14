/*
 * Forked from:
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/soundcloud/ad/HideAdsPatch.kt
 */
package app.morphe.patches.soundcloud.ad

import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.FeatureConstructorFingerprint

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide ads",
) {
    compatibleWith(AppCompatibilities.SOUNDCLOUD)

    execute {
        // Force-enable the no_audio_ads feature flag by writing directly to Feature.enabled.
        FeatureConstructorFingerprint.method.addInstructions(
            2,
            """
                const/4 p2, 0x1
                iput-boolean p2, p0, Lcom/soundcloud/android/configuration/plans/Feature;->enabled:Z
            """.trimIndent()
        )

        // Overwrite the JSON response from the server to a paid plan, which hides all ads in the app.
        UserConsumerPlanConstructorFingerprint.method.addInstructions(
            0,
            """
                const-string p1, "high_tier"
                new-instance p4, Ljava/util/ArrayList;
                invoke-direct { p4 }, Ljava/util/ArrayList;-><init>()V
                const-string p5, "go-plus"
                const-string p6, "SoundCloud Go+"
            """.trimIndent(),
        )

        // Prevent verification of an HTTP header containing the user's current plan.
        InterceptFingerprint.method.apply {
            val conditionIndex = InterceptFingerprint.instructionMatches.last().index + 1
            addInstruction(
                conditionIndex,
                "return-object p1",
            )
        }
    }
}

