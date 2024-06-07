    .comm x, 24, 8
    .globl main
main:
    enter $(8 * 6), $0
    /* $t1 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* %av0 = addressAt x, $t1 */
    /* AddressAt */
    movq x@GOTPCREL(%rip), %r11
    movq $1, %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -16(%rbp)
    /* $t0 = 42 */
    /* CopyInst */
    movq $42, %r10
    movq %r10, -24(%rbp)
    /* store $t0, %av0 */
    /* StoreInst */
    movq -24(%rbp), %r10
    movq -16(%rbp), %r11
    movq %r10, 0(%r11)
    /* $t3 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -32(%rbp)
    /* %av1 = addressAt x, $t3 */
    /* AddressAt */
    movq x@GOTPCREL(%rip), %r11
    movq $4, %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -40(%rbp)
    /* $t2 = load %av1 */
    /* LoadInst */
    movq -40(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -48(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t2) */
    movq -48(%rbp), %rdi
    call printInt
    leave
    ret
