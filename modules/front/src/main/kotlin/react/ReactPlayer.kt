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

@file:JsModule("react-player")
@file:JsNonModule

package react

@JsName("default")
external val reactPlayer: RClass<ReactPlayerProps>

external interface ReactPlayerProps : RProps {
    var url: String
    var playing: Boolean
    var loop: Boolean
    var controls: Boolean
    var light: Boolean
    var volume: Double?
    var muted: Boolean
    var playbackRate: Float
    var width: String
    var height: String
    var style: String
    var progressInterval: Int
    var playsinline: Boolean
    var pip: Boolean
    var wrapper: ReactElement
    var playIcon: ReactElement
    var config: Any
}
