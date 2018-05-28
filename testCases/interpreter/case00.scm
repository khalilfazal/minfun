(define (factorial x)
    (cond
        ((< x 2) 1)
        (else (* x (factorial (- x 1))))
    )
)

(factorial 10)
