    .globl main
main:
    enter $(8 * 6), $0
    /* $t1 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -8(%rbp)
    /* $a0 = $t1 */
    /* CopyInst */
    movq -8(%rbp), %r10
    movq %r10, -16(%rbp)
L2:
    /* $t2 = 3 */
    /* CopyInst */
    movq $3, %r10
    movq %r10, -24(%rbp)
    /* $t3 = $a0 >= $t2 */
    movq $0, %rax
    movq $1, %r10
    movq -16(%rbp), %r11
    cmp -24(%rbp) , %r11
    cmovge %r10, %rax
    movq %rax, -32(%rbp)
    /* jump $t3 */
    movq -32(%rbp), %r10
    cmp $1, %r10
    je L1
    /* call Symbol(printInt:func(TypeList(int)):void) ($a0) */
    movq -16(%rbp), %rdi
    call printInt
    movq %rax, -40(%rbp)
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    movq %rax, -40(%rbp)
    /* $t4 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -48(%rbp)
    /* $t5 = $a0 + $t4 */
    movq -16(%rbp), %r10
    addq -48(%rbp), %r10
    movq %r10, -56(%rbp)
    /* $a0 = $t5 */
    /* CopyInst */
    movq -56(%rbp), %r10
    movq %r10, -16(%rbp)
    jmp L2
L1:
    leave
    ret
