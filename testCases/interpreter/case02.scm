(define (f x)
    (cond
        ((= x 2) 2)
        ((> x 2) 3)
        ((< x 2) 1)
    )
)

(f 5)
(f 2)
(f 1)
