package app

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.{PrimaryStage, userAgentStylesheet_=}
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Scene
import scalafx.scene.paint.Color.*
import scalafx.animation.KeyFrame
import scalafx.animation.Timeline
import scalafx.animation.Timeline.*
import scalafx.util.Duration
import app.Direction.{DOWN, LEFT, RIGHT}
import scalafx.scene.paint.Color

import scala.util.Random

object Main extends JFXApp3 {

    private val windowSize = 600
    private val particleAmount = 50
    private val particleRadius = 5

    override def start(): Unit = {
        val particleList = List.fill(particleAmount) {
            Particle(
                Random.nextInt(10000),
                particleRadius,
                Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), 1),
                Coord(Random.nextInt(600), Random.nextInt(600)),
                Direction.fromOrdinal(Random.nextInt(8))
            )
        }

        val testList = List(
            Particle(
                1,
                particleRadius,
                Red,
                Coord(0, 300),
                RIGHT
            ),
            Particle(
                2,
                particleRadius,
                Green,
                Coord(600, 300),
                LEFT
            )
        )
        val simulation: ObjectProperty[Simulation] = ObjectProperty(Simulation(particleList))

        stage = new PrimaryStage {
            title = "MAS - Multi-agents System"
            width = windowSize
            height = windowSize
            scene = new Scene {
                fill = White
                content = simulation.value.draw()
                simulation.onChange {
                    content = simulation.value.draw()
                }
            }
        }

        new Timeline {
            keyFrames = List(
                KeyFrame(
                    time = Duration(20),
                    onFinished = _ => simulation.update(simulation.value.move(windowSize, particleRadius))
                )
            )
            cycleCount = Indefinite
        }.play()
    }
}


