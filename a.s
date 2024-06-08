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
    movq %rdi, -24(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($a0) */
    movq -24(%rbp), %rdi
    call printInt
    movq %rax, -32(%rbp)
    leave
    ret
    .globl myPrintTwo
myPrintTwo:
    enter $(8 * 2), $0
    movq %rdi, -40(%rbp)
    movq %rsi, -48(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($a0) */
    movq -40(%rbp), %rdi
    call printInt
    movq %rax, -56(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($b1) */
    movq -48(%rbp), %rdi
    call printInt
    movq %rax, -64(%rbp)
    leave
    ret
    .globl myPrintThree
myPrintThree:
    enter $(8 * 4), $0
    movq %rdi, -72(%rbp)
    movq %rsi, -80(%rbp)
    movq %rdx, -88(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($a0) */
    movq -72(%rbp), %rdi
    call printInt
    movq %rax, -96(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($b1) */
    movq -80(%rbp), %rdi
    call printInt
    movq %rax, -104(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($c2) */
    movq -88(%rbp), %rdi
    call printInt
    movq %rax, -112(%rbp)
    leave
    ret
    .globl main
main:
    enter $(8 * 6), $0
    /* call Symbol(myPrintZero:func(TypeList()):void) () */
    call myPrintZero
    movq %rax, -120(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -128(%rbp)
    /* $t0 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -136(%rbp)
    /* call Symbol(myPrintOne:func(TypeList(int)):void) ($t0) */
    movq -136(%rbp), %rdi
    call myPrintOne
    movq %rax, -144(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -152(%rbp)
    /* $t1 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -160(%rbp)
    /* $t2 = 2 */
    /* CopyInst */
    movq $2, %r10
    movq %r10, -168(%rbp)
    /* call Symbol(myPrintTwo:func(TypeList(int, int)):void) ($t1$t2) */
    movq -168(%rbp), %rsi
    movq -160(%rbp), %rdi
    call myPrintTwo
    movq %rax, -176(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -184(%rbp)
    /* $t3 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -192(%rbp)
    /* $t4 = 2 */
    /* CopyInst */
    movq $2, %r10
    movq %r10, -200(%rbp)
    /* $t5 = 3 */
    /* CopyInst */
    movq $3, %r10
    movq %r10, -208(%rbp)
    /* call Symbol(myPrintThree:func(TypeList(int, int, int)):void) ($t3$t4$t5) */
    movq -208(%rbp), %rdx
    movq -200(%rbp), %rsi
    movq -192(%rbp), %rdi
    call myPrintThree
    movq %rax, -216(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -224(%rbp)
    leave
    ret
