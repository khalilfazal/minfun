(define (all x y z)
    (= x y z)
)

(define (increasing x y z)
    (< x y z)
)

(define (decreasing x y z)
    (> x y z)
)

(define (increasingMonotone x y z)
    (<= x y z)
)

(define (decreasingMonotone x y z)
    (>= x y z)
)

(all 1 1 1)
(increasing 1 2 3)
(decreasing 3 2 1)
(increasingMonotone 1 1 2)
(decreasingMonotone 2 2 1)
