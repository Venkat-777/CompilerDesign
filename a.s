    .globl main
main:
    enter $(8 * 4), $0
    /* $t0 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* $t1 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -16(%rbp)
    /* $t2 = $t0 + $t1 */
    movq -16(%rbp), %r10
    addq -8(%rbp), %r10
    movq %r10, -24(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t2) */
    movq -24(%rbp), %rdi
    call printInt
    movq %rax, -32(%rbp)
    leave
    ret
