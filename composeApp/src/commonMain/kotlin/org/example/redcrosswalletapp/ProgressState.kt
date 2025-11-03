package org.example.redcrosswalletapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * State holder for progress tracking.
 * Manages a progress value in the range 0.0 to 1.0 (0% to 100%).
 * When progress reaches 100%, it automatically levels up and resets to 0%.
 *
 * @param initialProgress Starting progress value (default: 0f)
 * @param initialLevel   Starting level (default: 1)
 */
class ProgressState(
    initialProgress: Float = 0f,
    initialLevel: Int = 1,
) {

    // -----------------------------------------------------------------
    // 1️⃣ Internal double‑precision accumulator (avoids rounding drift)
    // -----------------------------------------------------------------
    private var internalProgress: Double =
        (initialProgress.coerceIn(MIN_PROGRESS, MAX_PROGRESS)).toDouble()

    // -----------------------------------------------------------------
    // 2️⃣ Public observable flows for progress & level
    // -----------------------------------------------------------------
    private val _progress = MutableStateFlow(initialProgress.coerceIn(MIN_PROGRESS, MAX_PROGRESS))
    private val _level = MutableStateFlow(initialLevel.coerceIn(1, MAX_LEVEL))

    val progress: StateFlow<Float> = _progress.asStateFlow()
    val level: StateFlow<Int> = _level.asStateFlow()

    // -----------------------------------------------------------------
    // 3️⃣ Max‑level flag (exposed as a flow)
    // -----------------------------------------------------------------
    private val _isMaxLevel = MutableStateFlow(isMaxLevel())
    val isMaxLevelFlow: StateFlow<Boolean> = _isMaxLevel.asStateFlow()

    // -----------------------------------------------------------------
    // 4️⃣ Cosmetic image carousel index (now a **MutableStateFlow**)
    // -----------------------------------------------------------------
    private val _cosmeticImageIndex = MutableStateFlow(0)   // <-- correct type
    /** Public flow that UI can collect. */
    val cosmeticImageIndexFlow: StateFlow<Int> = _cosmeticImageIndex.asStateFlow()

    /**
     * Call this when the user taps the “Next cosmetic” button.
     *
     * @param totalImages The number of images in the carousel.
     */
    fun advanceCosmeticImage(totalImages: Int) {
        if (totalImages <= 0) return               // safety guard
        _cosmeticImageIndex.value = (_cosmeticImageIndex.value + 1) % totalImages
    }

    // -----------------------------------------------------------------
    // 5️⃣ Public API – callers continue to use Float for progress
    // -----------------------------------------------------------------
    fun advance(amount: Float) {
        internalProgress += amount.toDouble()
        normalizeAndEmit()
    }

    fun setProgress(value: Float) {
        val newProgress = value.coerceIn(MIN_PROGRESS, MAX_PROGRESS)

        // If we cross the 100% threshold and aren't maxed out,
        // trigger a level‑up.
        if (newProgress >= MAX_PROGRESS && _level.value < MAX_LEVEL) {
            levelUp()
        } else if (newProgress < MAX_PROGRESS && _level.value >= MAX_LEVEL) {
            // At max level we just cap at 100%
            _progress.value = MAX_PROGRESS
        } else {
            _progress.value = newProgress
        }
    }

    /** Returns true when the current level is the maximum allowed. */
    fun isMaxLevel(): Boolean = _level.value >= MAX_LEVEL

    // -----------------------------------------------------------------
    // 6️⃣ Private helpers
    // -----------------------------------------------------------------
    private fun normalizeAndEmit() {
        // Consume whole levels while we have enough progress.
        while (internalProgress >= 1.0 && _level.value < MAX_LEVEL) {
            internalProgress -= 1.0
            levelUp()
        }

        // Clamp at max level if we’re already there.
        if (_level.value >= MAX_LEVEL && internalProgress > 1.0) {
            internalProgress = 1.0
        }

        // Emit the Float version for UI observers.
        _progress.value = internalProgress.toFloat()
    }

    private fun levelUp() {
        if (_level.value < MAX_LEVEL) {
            _level.value += 1
            // Reset progress for the next level.
            _progress.value = MIN_PROGRESS

            // If we just reached the maximum level, flip the flag.
            if (_level.value >= MAX_LEVEL) {
                _isMaxLevel.value = true
            }
        }
    }

    private fun updateProgress(increment: Float) {
        val newProgress = _progress.value + increment
        setProgress(newProgress)
    }

    // -----------------------------------------------------------------
    // 7️⃣ Constants
    // -----------------------------------------------------------------
    companion object {
        private const val MIN_PROGRESS = 0f
        private const val MAX_PROGRESS = 1f
        private const val MAX_LEVEL = 4 // 1=sprout, 2=sprout_1, 3=sprout_2, 4=tree
    }
}