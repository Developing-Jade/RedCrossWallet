package org.example.redcrosswalletapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * State holder for progress tracking.
 * Manages a progress value in the range 0.0 to 1.0 (0% to 100%).
 *
 * @param initialProgress Starting progress value (default: 0f)
 */
class ProgressState(initialProgress: Float = 0f) {

    private val _progress = MutableStateFlow(initialProgress.coerceIn(MIN_PROGRESS, MAX_PROGRESS))

    /**
     * Observable progress value between 0.0 and 1.0
     */
    val progress: StateFlow<Float> = _progress.asStateFlow()

    /**
     * Advances progress by 25% (0.25), clamping at 100%
     */
    fun advanceQuarter() {
        updateProgress(QUARTER_INCREMENT)
    }

    /**
     * Advances progress by a custom amount
     *
     * @param amount The amount to add (will be clamped to valid range)
     */
    fun advance(amount: Float) {
        updateProgress(amount)
    }

    /**
     * Sets progress to a specific value
     *
     * @param value The new progress value (will be clamped between 0.0 and 1.0)
     */
    fun setProgress(value: Float) {
        _progress.value = value.coerceIn(MIN_PROGRESS, MAX_PROGRESS)
    }

    /**
     * Resets progress back to 0%
     */
    fun reset() {
        _progress.value = MIN_PROGRESS
    }

    /**
     * Checks if progress is complete (100%)
     */
    fun isComplete(): Boolean = _progress.value >= MAX_PROGRESS

    private fun updateProgress(increment: Float) {
        _progress.value = (_progress.value + increment).coerceIn(MIN_PROGRESS, MAX_PROGRESS)
    }

    companion object {
        private const val MIN_PROGRESS = 0f
        private const val MAX_PROGRESS = 1f
        private const val QUARTER_INCREMENT = 0.25f
    }
}