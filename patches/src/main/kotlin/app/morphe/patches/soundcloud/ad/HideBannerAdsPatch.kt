package app.morphe.patches.soundcloud.ad

import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.findMutableMethodOf

@Suppress("unused")
val hideBannerAdsPatch = bytecodePatch(
    name = "Hide banner ads",
) {
    compatibleWith(AppCompatibilities.SOUNDCLOUD)

    execute {
        val classDef = BannerAdFetchConditionsFingerprint.classDef
        val mutableClass = mutableClassDefBy(classDef)

        classDef.methods
            .filter { it.returnType == "Z" && it.parameterTypes.isEmpty() }
            .forEach { method ->
                mutableClass.findMutableMethodOf(method).addInstructions(
                    0,
                    """
                        const/4 p0, 0x0
                        return p0
                    """.trimIndent(),
                )
            }
    }
}
