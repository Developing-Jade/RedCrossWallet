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
 * @param initialLevel Starting level (default: 1)
 */
class ProgressState(
    initialProgress: Float = 0f,
    initialLevel: Int = 1,
) {

    // Cast initialProgress to Double to avoid floating-point precision issues
    private var internalProgress: Double =
        (initialProgress.coerceIn(MIN_PROGRESS, MAX_PROGRESS)).toDouble()


    // Public observable flows for progress and level
    private val _progress = MutableStateFlow(initialProgress.coerceIn(MIN_PROGRESS, MAX_PROGRESS))
    private val _level = MutableStateFlow(initialLevel.coerceIn(1, MAX_LEVEL))

    /**
     * Observable progress value between 0.0 and 1.0
     */
    val progress: StateFlow<Float> = _progress.asStateFlow()

    /**
     * Observable level value
     */
    val level: StateFlow<Int> = _level.asStateFlow()


    // Public API - callers continue to use "Float" for progress
    /**
     * Advances progress by a custom amount with auto level-up
     *
     * @param amount The amount to add
     */
    fun advance(amount: Float) {
        // Convert amount to Double
        internalProgress += amount.toDouble()
        normalizeAndEmit()
    }

    /**
     * Sets progress to a specific value with auto level-up
     *
     * @param value The new progress value (will be clamped between 0.0 and 1.0)
     */
    fun setProgress(value: Float) {
        val newProgress = value.coerceIn(MIN_PROGRESS, MAX_PROGRESS)

        // Check if we've completed a level
        if (newProgress >= MAX_PROGRESS && _level.value < MAX_LEVEL) {
            levelUp()
        } else if (newProgress < MAX_PROGRESS && _level.value >= MAX_LEVEL) {
            // At max level, just cap @ 100%
            _progress.value = MAX_PROGRESS
        } else {
            _progress.value = newProgress
        }
    }

    /**
     * Resets progress back to 0% and resets level to 1
     */
    fun reset() {
        _progress.value = MIN_PROGRESS
        _level.value = 1
    }

    /**
     * Checks if progress is complete (100%)
     */
    fun isComplete(): Boolean = _progress.value >= MAX_PROGRESS

    /**
     * Checks if at max level
     */
    fun isMaxLevel(): Boolean = _level.value >= MAX_LEVEL

    private fun normalizeAndEmit() {
        // While we have more progress than a full level, normalize to 100%
        // Consume 1 level at a time
        while (internalProgress >= 1.0 && _level.value < MAX_LEVEL) {
            internalProgress -= 1.0
            levelUp()
        }

        // If at max level, cap at 100%
        if (_level.value >= MAX_LEVEL && internalProgress > 1.0) {
            internalProgress = 1.0
        }

        // Emit the normalized progress value for UI observation
        _progress.value = internalProgress.toFloat()
    }


    /**
     * Levels up: increases level by 1 and resets progress to 0%
     */
    private fun levelUp() {
        if (_level.value < MAX_LEVEL) {
            _level.value += 1
            // Reset progress to 0%
            // The level has been incremented, so we can safely reset progress
            _progress.value = MIN_PROGRESS
        }
    }

    private fun updateProgress(increment: Float) {
        val newProgress = _progress.value + increment
        setProgress(newProgress)
    }

    companion object {
        private const val MIN_PROGRESS = 0f
        private const val MAX_PROGRESS = 1f
        private const val MAX_LEVEL = 4 // 1=sprout, 2=sprout_1, 3=sprout_2, 4=tree
    }
}