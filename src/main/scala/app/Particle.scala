package app

import scalafx.scene.paint.Color

final case class Particle(id: Int, radius: Int, color: Color, position: Coord, direction: Direction)
