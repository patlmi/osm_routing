 /*
  * Copyright 2011 Andlabs, GbR.
  *
  * This is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * OsmRouting is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
  */

package de.andlabs.routing

import net.pragyah.scalgorithms.heaps.FibonacciHeap
import scala.collection.mutable.{ListBuffer, Map}
import Graph.Node


object Dijkstra {


	def getSPT(source: Int, target: Int): Map[Int, Node] = {

		val heap = new FibonacciHeap[Node](Node.min)
		val visited = Map[Int, Node]()
		
		val s = Node(source, dist = 0)
		visited(s.id) = s
		heap.insert(s)

		while (!heap.empty) {

			var node = heap.extractMin.get
			node.settled = true

			if (node.id == target) {
				return visited
			}

			node.neighbours { (weight, id) =>

				val neighbour = Node(id)
				neighbour.pred = node.id
				neighbour.dist = node.dist + weight

				visited.get(id) match {

					case Some(visited) if visited.settled =>
					case Some(visited) if (neighbour.dist < visited.dist) =>
						heap.decreaseKey(visited, neighbour)
					case None =>
						heap.insert(neighbour)
						visited(neighbour.id) = neighbour
					case _ => // println("nothing to do")
				}
			}			
		}
		println("NO PATH FOUND!")
		visited
	}

	def shortestPath(source: Int, target: Int): List[Int] = {

		val spt = getSPT(source, target)
		val path = ListBuffer[Int]()

		var node = spt(target)
		while (node.pred != 0) {
			path += node.id
			node = spt(node.pred)
		}
		path += node.id
		return path.toList
	}

	def apply(source: Int, target: Int): List[Int] = {
		shortestPath(source, target)
	}
}

