package app

import app.Direction.{DOWN, DOWN_LEFT, DOWN_RIGHT, LEFT, RIGHT, UP, UP_LEFT, UP_RIGHT}
import scalafx.scene.shape.Circle

import scala.util.Random

final case class Simulation(particleList: List[Particle]) {

    def draw(): List[Circle] = {

        val shapeList = particleList.map { p =>
            new Circle {
                centerX = p.position.x
                centerY = p.position.y
                radius = p.radius
                fill = p.color
            }
        }

        shapeList
    }

    def move(windowSize: Int, agentRadius: Int): Simulation = {
        val newParticleList = particleList.map { p =>
            val newCoord = computeNewCoord(p, windowSize)

            p.copy(position = newCoord)
        }

        val postColParticleList = newParticleList.map { p =>
            // Collisions (from https://www.jeffreythompson.org/collision-detection/circle-circle.php)
            if (particleList.exists(ple =>
                val distance = getCirclesDistance(ple, p)
                //println(distance)
                distance <= (agentRadius * 2) && p.id != ple.id

            )) {
                // Nouvelle direction en excluant l'actuelle et les deux adjacentes
                val availableDirections = Direction.values.filter(d => d.ordinal != p.direction.ordinal)
                val newDir = availableDirections(Random.nextInt(7))

                val offset = 1
                val postColCoord = newDir match {
                    case UP => p.position.copy(p.position.x, p.position.y - offset)
                    case UP_RIGHT => p.position.copy(p.position.x + offset, p.position.y - offset)
                    case RIGHT => p.position.copy(p.position.x + offset, p.position.y)
                    case DOWN_RIGHT => p.position.copy(p.position.x + offset, p.position.y + offset)
                    case DOWN => p.position.copy(p.position.x, p.position.y + offset)
                    case DOWN_LEFT => p.position.copy(p.position.x - offset, p.position.y + offset)
                    case LEFT => p.position.copy(p.position.x - offset, p.position.y)
                    case UP_LEFT => p.position.copy(p.position.x - offset, p.position.y - offset)
                }

                println(s"${p.direction} -> $newDir")

                p.copy(position = postColCoord, direction = newDir)
            }
            else {
                p
            }
        }

        copy(particleList = postColParticleList);
    }

    private def computeNewCoord(p: Particle, windowSize: Int): Coord = {
        // Respawn aux bords
        val aboveX = p.position.x > windowSize
        val aboveY = p.position.y > windowSize

        val belowX = p.position.x < 0
        val belowY = p.position.y < 0

        if (aboveX || aboveY) {
            Coord(if(aboveX) 0 else p.position.x, if(aboveY) 0 else p.position.y)
        }
        else if (belowX || belowY) {
            Coord(if(belowX) windowSize else p.position.x, if(belowY) windowSize else p.position.y)
        }
        // Direction aléatoire
        else {
            p.direction match {
                case UP => Coord(p.position.x, p.position.y - 1)
                case UP_RIGHT => Coord(p.position.x + 1, p.position.y - 1)
                case RIGHT => Coord(p.position.x + 1, p.position.y)
                case DOWN_RIGHT => Coord(p.position.x + 1, p.position.y + 1)
                case DOWN => Coord(p.position.x, p.position.y + 1)
                case DOWN_LEFT => Coord(p.position.x - 1, p.position.y + 1)
                case LEFT => Coord(p.position.x - 1, p.position.y)
                case UP_LEFT => Coord(p.position.x - 1, p.position.y - 1)
            }
        }
    }

    private def getCirclesDistance(p1: Particle, p2: Particle): Double = {
        val distX = p1.position.x - p2.position.x
        val distY = p1.position.y - p2.position.y
        Math.sqrt((distX * distX) + (distY * distY))
    }
}
