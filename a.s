    .globl myPrintZero
myPrintZero:
    enter $(8 * 2), $0
    /* $t0 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -8(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t0) */
    movq -8(%rbp), %rdi
    call printInt
    movq %rax, -16(%rbp)
    leave
    ret
    .globl myPrintOne
myPrintOne:
    enter $(8 * 2), $0
    movq %rdi, -8(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($a0) */
    movq -8(%rbp), %rdi
    call printInt
    movq %rax, -16(%rbp)
    leave
    ret
    .globl myPrintTwo
myPrintTwo:
    enter $(8 * 2), $0
    movq %rdi, -8(%rbp)
    movq %rsi, -16(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($a0) */
    movq -8(%rbp), %rdi
    call printInt
    movq %rax, -16(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($b1) */
    movq -16(%rbp), %rdi
    call printInt
    movq %rax, -16(%rbp)
    leave
    ret
    .globl myPrintThree
myPrintThree:
    enter $(8 * 4), $0
    movq %rdi, -8(%rbp)
    movq %rsi, -16(%rbp)
    movq %rdx, -24(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($a0) */
    movq -8(%rbp), %rdi
    call printInt
    movq %rax, -16(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($b1) */
    movq -16(%rbp), %rdi
    call printInt
    movq %rax, -16(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($c2) */
    movq -24(%rbp), %rdi
    call printInt
    movq %rax, -16(%rbp)
    leave
    ret
    .globl main
main:
    enter $(8 * 6), $0
    /* call Symbol(myPrintZero:func(TypeList()):void) () */
    call myPrintZero
    movq %rax, -16(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -16(%rbp)
    /* $t0 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* call Symbol(myPrintOne:func(TypeList(int)):void) ($t0) */
    movq -8(%rbp), %rdi
    call myPrintOne
    movq %rax, -16(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -16(%rbp)
    /* $t1 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -16(%rbp)
    /* $t2 = 2 */
    /* CopyInst */
    movq $2, %r10
    movq %r10, -24(%rbp)
    /* call Symbol(myPrintTwo:func(TypeList(int, int)):void) ($t1$t2) */
    movq -24(%rbp), %rsi
    movq -16(%rbp), %rdi
    call myPrintTwo
    movq %rax, -16(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -16(%rbp)
    /* $t3 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -32(%rbp)
    /* $t4 = 2 */
    /* CopyInst */
    movq $2, %r10
    movq %r10, -40(%rbp)
    /* $t5 = 3 */
    /* CopyInst */
    movq $3, %r10
    movq %r10, -48(%rbp)
    /* call Symbol(myPrintThree:func(TypeList(int, int, int)):void) ($t3$t4$t5) */
    movq -48(%rbp), %rdx
    movq -40(%rbp), %rsi
    movq -32(%rbp), %rdi
    call myPrintThree
    movq %rax, -16(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -16(%rbp)
    leave
    ret
