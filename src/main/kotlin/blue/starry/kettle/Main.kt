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

package blue.starry.kettle

import electron.BrowserWindow
import electron.Menu
import electron.app
import node.dirname
import node.process
import kotlin.browser.window
import kotlin.js.json

var mainWindow: dynamic = null

fun main() {
    // This method will be called when Electron has finished
    // initialization and is ready to create browser windows.
    // Some APIs can only be used after this event occurs.
    app.on("ready", ::createMainWindow)

    // Need for macOS
    app.on("window-all-closed") {
        // On OS X it is common for applications and their menu bar
        // to stay active until the user quits explicitly with Cmd + Q
        if (process.platform !== "darwin") {
            app.quit()
        }
    }
    app.on("activate") {
        // On OS X it's common to re-create a window in the app when the
        // dock icon is clicked and there are no other windows open.
        if (mainWindow === null) {
            createMainWindow()
        }
    }
}

private fun createMainWindow() {
    mainWindow = BrowserWindow {
        "width" set 800
        "height" set 600
        "webPreferences" set json(
            "nodeIntegration" to true,
            "enableRemoteModule" to false
        )
    }

    mainWindow.loadURL("file://$dirname/index.html")

    // Hide menu bar
    Menu.setApplicationMenu(null)

    mainWindow.on("unresponsive") {
        window.alert("Browser seems to crash.")
    }

    mainWindow.on("closed", fun () {
        mainWindow = null
    })
}

// Alternative version using 'datauri' and 'kotlinx-html'
//fun loadIndex() {
//
//    fun encodeAsUrl(html: String): String {
//        return js("""
//        var DataUri = require('datauri');
//        var datauri = new DataUri();
//        datauri.format('.html', html);
//        datauri.content
//    """).toString()
//    }
//
//    val htmlString = createHTML(prettyPrint = true).html {
//        head {
//            meta(name = "test", content = "debug")
//        }
//        body {
//            h1 {
//                +"Hello World!"
//            }
//            p {
//                +"Welcome"
//            }
//        }
//    }
//    mainWindow?.loadURL(encodeAsUrl(htmlString))
//}
