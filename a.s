    .globl main
main:
    enter $(8 * 6), $0
    /* $t0 = true */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* jump $t0 */
    movq -8(%rbp), %r10
    cmp $1, %r10
    je L1
L2:
    /* $t2 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -16(%rbp)
    /* $t3 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -24(%rbp)
    /* $t4 = $t2 - $t3 */
    movq -16(%rbp), %r10
    subq -24(%rbp), %r10
    movq %r10, -32(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t4) */
    movq -32(%rbp), %rdi
    call printInt
    movq %rax, -40(%rbp)
    leave
    ret
L1:
    /* $t1 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -48(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t1) */
    movq -48(%rbp), %rdi
    call printInt
    movq %rax, -56(%rbp)
    jmp L2
