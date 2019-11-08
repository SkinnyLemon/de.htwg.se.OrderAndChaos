import scala.util.Random
val size = 128
def rowSum(row: Vector[Int], column: Vector[Int]): Int = (for (i <- 0 until size) yield row(i) * column(i)).sum

val values = Vector.fill(size)(Random.nextInt)
val initialMatrix = Array.ofDim[Int](size, size)
for (i <- 0 until size; j <- 0 until size) {
  initialMatrix(i)(j) = values((i + j) % size)
}
val A = initialMatrix.map(row => row.toVector).toVector
val b = Vector.fill(size)(Random.nextInt)

val solution = (for (i <- 0 until size)  yield rowSum(A(i), b)).toVector

val products = (for (i <- 0 until size; j <- 0 until size) yield A(i)(j) * b(i)).toVector

products.exists(value => products.count(_ equals value) > 1)
