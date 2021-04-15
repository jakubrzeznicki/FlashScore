package com.kuba.flashscore.util

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object MatcherUtils {

    fun withToolbarTitle(title: CharSequence?): Matcher<View?>? {
        return withToolbarTitle(Matchers.`is`(title))
    }

    private fun withToolbarTitle(textMatcher: Matcher<CharSequence?>): Matcher<View?>? {
        return object : BoundedMatcher<View?, Toolbar>(Toolbar::class.java) {
            override fun matchesSafely(toolbar: Toolbar): Boolean {
                return textMatcher.matches(toolbar.title)
            }

            override fun describeTo(description: Description) {
                description.appendText("with toolbar subtitle: ")
                textMatcher.describeTo(description)
            }
        }
    }

    fun withToolbarSubTitle(title: CharSequence?): Matcher<View?>? {
        return withToolbarSubTitle(Matchers.`is`(title))
    }

    private fun withToolbarSubTitle(textMatcher: Matcher<CharSequence?>): Matcher<View?>? {
        return object : BoundedMatcher<View?, Toolbar>(Toolbar::class.java) {
            override fun matchesSafely(toolbar: Toolbar): Boolean {
                return textMatcher.matches(toolbar.subtitle)
            }

            override fun describeTo(description: Description) {
                description.appendText("with toolbar subtitle: ")
                textMatcher.describeTo(description)
            }
        }
    }
}