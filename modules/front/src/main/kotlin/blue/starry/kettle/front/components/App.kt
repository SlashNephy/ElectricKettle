/*
 * The MIT License (MIT)
 *
 *     Copyright (c) 2020 StarryBlueSky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package blue.starry.kettle.front.components

import blue.starry.kettle.front.TEST_CLIENT
import blue.starry.penicillin.endpoints.statuses
import blue.starry.penicillin.endpoints.statuses.create
import blue.starry.penicillin.endpoints.timeline
import blue.starry.penicillin.endpoints.timeline.homeTimeline
import blue.starry.penicillin.endpoints.timeline.mentionsTimeline
import blue.starry.penicillin.endpoints.timeline.userTimelineByScreenName
import blue.starry.penicillin.extensions.execute
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.pct
import kotlinx.css.width
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import styled.css
import styled.styledDiv
import styled.styledTextArea

interface AppState: RState {
    var text: String?
}

class App: RComponent<RProps, AppState>() {
    companion object {
        val Scope = MainScope()
    }

    override fun AppState.init() {
        text = null
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = Display.inlineFlex
                width = 100.pct
            }

            styledTextArea(rows = "3") {
                attrs.id = "composer"
                +"What's happening?"

                css {
                    width = 100.pct
                    put("resize", "none")
                }

                attrs {
                    onChangeFunction = {
                        state.text = it.target.asDynamic().value as String?
                    }
                }
            }

            button {
                +"Tweet"

                attrs {
                    onClickFunction = {
                        val text = state.text
                        if (text != null) {
                            Scope.launch {
                                TEST_CLIENT.statuses.create(
                                    status = text
                                ).execute()
                            }
                        }
                    }
                }
            }
        }

        styledDiv {
            css {
                display = Display.flex
            }

            tweetColumn {
                title = "Home TL"
                fetcher = {
                    val response = TEST_CLIENT.timeline.homeTimeline.execute()
                    response.results
                }
            }

            tweetColumn {
                title = "Mention TL"
                fetcher = {
                    val response = TEST_CLIENT.timeline.mentionsTimeline.execute()
                    response.results
                }
            }

            tweetColumn {
                title = "User TL"
                fetcher = {
                    val response = TEST_CLIENT.timeline.userTimelineByScreenName("samekan822").execute()
                    response.results
                }
            }
        }
    }
}
