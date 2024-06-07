    .comm addi, 8, 8
    .globl main
main:
    enter $(8 * 4), $0
    /* $t0 = call Symbol(readInt:func(TypeList()):int) () */
    call readInt
    movq %rax, -8(%rbp)
    /* %av0 = addressAt addi, null */
    /* AddressAt */
    movq addi@GOTPCREL(%rip), %r11
    movq %r11, -16(%rbp)
    /* store $t0, %av0 */
    /* StoreInst */
    movq -8(%rbp), %r10
    movq -16(%rbp), %r11
    movq %r10, 0(%r11)
    /* %av1 = addressAt addi, null */
    /* AddressAt */
    movq addi@GOTPCREL(%rip), %r11
    movq %r11, -24(%rbp)
    /* $t1 = load %av1 */
    /* LoadInst */
    movq -24(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -32(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t1) */
    movq -32(%rbp), %rdi
    call printInt
    movq %rax, -40(%rbp)
    leave
    ret
