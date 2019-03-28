package app

import app.view.MainView
import app.view.Styles
import tornadofx.App

class AppStarter: App(MainView::class, Styles::class)