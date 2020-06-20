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

import blue.starry.penicillin.extensions.models.text
import blue.starry.penicillin.extensions.via
import blue.starry.penicillin.models.Status
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.borderRight
import kotlinx.css.properties.textDecoration
import react.*
import react.dom.*
import styled.css
import styled.styledImg
import styled.styledP
import styled.styledSection


interface TweetColumnProps: RProps {
    var title: String
    var fetcher: suspend () -> List<Status>
}

interface TweetColumnState: RState {
    var statuses: List<Status>
}

class TweetCellColumn: RComponent<TweetColumnProps, TweetColumnState>() {
    override fun TweetColumnState.init() {
        statuses = listOf()

        App.Scope.launch {
            val result = props.fetcher()

            setState {
                statuses = result
            }
        }
    }

    override fun RBuilder.render() {
        styledSection {
            css {
                maxWidth = 400.px
                padding(horizontal = 1.em)
                borderRight(2.px, BorderStyle.dashed, Color("#ccc"))
            }

            styledP {
                css {
                    textDecoration(TextDecorationLine.underline)
                }

                +props.title
            }

            state.statuses.forEach {
                div(classes = "tweet-cell") {
                    child(TweetCellContent::class) {
                        attrs {
                            status = it
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.tweetColumn(handler: TweetColumnProps.() -> Unit): ReactElement {
    return child(TweetCellColumn::class) {
        attrs(handler)
    }
}

interface TweetCellProps: RProps {
    var status: Status
}

class TweetCellContent: RComponent<TweetCellProps, RState>() {
    override fun RBuilder.render() {
        p(classes = "tweet-author") {
            img(classes = "icon", src = props.status.user.profileImageUrlHttps) {}

            span(classes = "name") {
                +"${props.status.user.name} @${props.status.user.screenName}"
            }
        }

        p(classes = "tweet-content") {
            span(classes = "text") {
                +props.status.text
            }

            div {
                props.status.entities.media.forEach {
                    styledImg(src = it.mediaUrlHttps) {
                        css {
                            width = 100.pct
                        }
                    }
                }
            }

            span {
                a(href = props.status.via.url) {
                    +props.status.via.name
                }
            }
        }
    }
}
