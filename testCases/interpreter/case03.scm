(define (f x)
    (cond
        ((>= x 4) #t)
        ((<= x 2) #f)
        (else 3)
    )
)

(f 5)
(f 4)
(f 3)
(f 2)
