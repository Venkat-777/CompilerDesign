    .globl paramtest
paramtest:
    enter $(8 * 10), $0
    movq %rdi, -8(%rbp)
    movq %rsi, -16(%rbp)
    movq %rdx, -24(%rbp)
    movq %rcx, -32(%rbp)
    movq %r8, -40(%rbp)
    movq %r9, -48(%rbp)
    movq -8(%rbp), %rdi
    call printInt
    call println
    movq -16(%rbp), %rdi
    call printInt
    call println
    movq -24(%rbp), %rdi
    call printInt
    call println
    movq -32(%rbp), %rdi
    call printInt
    call println
    movq -40(%rbp), %rdi
    call printInt
    call println
    movq -48(%rbp), %rdi
    call printInt
    call println
    movq 16(%rbp), %rdi
    call printInt
    call println
    movq 24(%rbp), %rdi
    call printInt
    call println
    movq 32(%rbp), %rdi
    call printInt
    call println
    leave
    ret
    .globl main
main:
    enter $(8 * 10), $0
    /* $t0 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* $t1 = 2 */
    /* CopyInst */
    movq $2, %r10
    movq %r10, -16(%rbp)
    /* $t2 = 3 */
    /* CopyInst */
    movq $3, %r10
    movq %r10, -24(%rbp)
    /* $t3 = 4 */
    /* CopyInst */
    movq $4, %r10
    movq %r10, -32(%rbp)
    /* $t4 = 5 */
    /* CopyInst */
    movq $5, %r10
    movq %r10, -40(%rbp)
    /* $t5 = 6 */
    /* CopyInst */
    movq $6, %r10
    movq %r10, -48(%rbp)
    /* $t6 = 7 */
    /* CopyInst */
    movq $7, %r10
    movq %r10, -56(%rbp)
    /* $t7 = 8 */
    /* CopyInst */
    movq $8, %r10
    movq %r10, -64(%rbp)
    /* $t8 = 9 */
    /* CopyInst */
    movq $9, %r10
    movq %r10, -72(%rbp)
    movq -8(%rbp), %rdi
    movq -16(%rbp), %rsi
    movq -24(%rbp), %rdx
    movq -32(%rbp), %rcx
    movq -40(%rbp), %r8
    movq -48(%rbp), %r9
    subq $32, %rsp
    movq -72(%rbp), %r10
    movq %r10, 16(%rsp)
    movq -64(%rbp), %r10
    movq %r10, 8(%rsp)
    movq -56(%rbp), %r10
    movq %r10, 0(%rsp)
    call paramtest
    addq $32, %rsp
    leave
    ret
