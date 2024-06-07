    .comm a, 8, 8
    .globl main
main:
    enter $(8 * 26), $0
    /* $t0 = 20 */
    /* CopyInst */
    movq $20, %r10
    movq %r10, -8(%rbp)
    /* %av0 = addressAt a, null */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq %r11, -16(%rbp)
    /* store $t0, %av0 */
    /* StoreInst */
    movq -8(%rbp), %r10
    movq -16(%rbp), %r11
    movq %r10, 0(%r11)
L3:
    /* %av1 = addressAt a, null */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq %r11, -24(%rbp)
    /* $t1 = load %av1 */
    /* LoadInst */
    movq -24(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -32(%rbp)
    /* $t2 = 10 */
    /* CopyInst */
    movq $10, %r10
    movq %r10, -40(%rbp)
    /* $t3 = $t1 > $t2 */
    movq $0, %rax
    movq $1, %r10
    movq -32(%rbp), %r10
    movq -40(%rbp), %r11
    cmovg %r10, %rax
    movq %rax, -48(%rbp)
    /* jump $t3 */
    movq -48(%rbp), %r10
    cmp $1, %r10
    je L1
    /* %av5 = addressAt a, null */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq %r11, -56(%rbp)
    /* $t12 = load %av5 */
    /* LoadInst */
    movq -56(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -64(%rbp)
    /* $t13 = 4 */
    /* CopyInst */
    movq $4, %r10
    movq %r10, -72(%rbp)
    /* $t14 = $t12 - $t13 */
    movq -64(%rbp), %r10
    subq -72(%rbp), %r10
    movq %r10, -80(%rbp)
    /* %av6 = addressAt a, null */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq %r11, -88(%rbp)
    /* store $t14, %av6 */
    /* StoreInst */
    movq -80(%rbp), %r10
    movq -88(%rbp), %r11
    movq %r10, 0(%r11)
    /* %av7 = addressAt a, null */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq %r11, -96(%rbp)
    /* $t15 = load %av7 */
    /* LoadInst */
    movq -96(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -104(%rbp)
    /* $t16 = 5 */
    /* CopyInst */
    movq $5, %r10
    movq %r10, -112(%rbp)
    /* $t17 = $t15 < $t16 */
    movq $0, %rax
    movq $1, %r10
    movq -104(%rbp), %r10
    movq -112(%rbp), %r11
    cmovg %r10, %rax
    movq %rax, -120(%rbp)
    /* jump $t17 */
    movq -120(%rbp), %r10
    cmp $1, %r10
    je L4
    jmp L3
L4:
L5:
    leave
    ret
L1:
    /* %av2 = addressAt a, null */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq %r11, -128(%rbp)
    /* $t4 = load %av2 */
    /* LoadInst */
    movq -128(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -136(%rbp)
    /* $t5 = 5 */
    /* CopyInst */
    movq $5, %r10
    movq %r10, -144(%rbp)
    /* $t6 = $t4 < $t5 */
    movq $0, %rax
    movq $1, %r10
    movq -136(%rbp), %r10
    movq -144(%rbp), %r11
    cmovg %r10, %rax
    movq %rax, -152(%rbp)
    /* jump $t6 */
    movq -152(%rbp), %r10
    cmp $1, %r10
    je L2
    /* %av3 = addressAt a, null */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq %r11, -160(%rbp)
    /* $t7 = load %av3 */
    /* LoadInst */
    movq -160(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -168(%rbp)
    /* $t8 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -176(%rbp)
    /* $t9 = 2 */
    /* CopyInst */
    movq $2, %r10
    movq %r10, -184(%rbp)
    /* $t10 = $t8 - $t9 */
    movq -176(%rbp), %r10
    subq -184(%rbp), %r10
    movq %r10, -192(%rbp)
    /* $t11 = $t7 / $t10 */
    movq -168(%rbp), %r10
    movq 21(%rbp), %rax
    cqto
    idivq 24(%rbp)
    movq %rax, 25(%rbp)
    movq %r10, -200(%rbp)
    /* %av4 = addressAt a, null */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq %r11, -208(%rbp)
    /* store $t11, %av4 */
    /* StoreInst */
    movq -200(%rbp), %r10
    movq -208(%rbp), %r11
    movq %r10, 0(%r11)
    jmp L3
L2:
    jmp L5
