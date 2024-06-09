    .comm a, 112, 8
    .globl bubblesort
bubblesort:
    enter $(8 * 30), $0
    /* $t1 = true */
    /* CopyInst     */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* $swapped0 = $t1 */
    /* CopyInst */
    movq -8(%rbp), %r10
    movq %r10, -16(%rbp)
L3:
    /* $t2 = not $swapped0 */
    movq $1, %r11
    subq $swapped0, %r11
    /* jump $t2 */
    movq -24(%rbp), %r10
    cmp $1, %r10
    je L1
    /* $t3 = false */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -32(%rbp)
    /* $swapped0 = $t3 */
    /* CopyInst */
    movq -32(%rbp), %r10
    movq %r10, -16(%rbp)
    /* $t5 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -40(%rbp)
    /* $i4 = $t5 */
    /* CopyInst */
    movq -40(%rbp), %r10
    movq %r10, -48(%rbp)
L5:
    /* $t6 = 13 */
    /* CopyInst */
    movq $13, %r10
    movq %r10, -56(%rbp)
    /* $t7 = $i4 >= $t6 */
    movq $0, %rax
    movq $1, %r10
    movq -48(%rbp), %r11
    cmp -56(%rbp) , %r11
    cmovge %r10, %rax
    movq %rax, -64(%rbp)
    /* jump $t7 */
    movq -64(%rbp), %r10
    cmp $1, %r10
    je L2
    /* %av0 = addressAt a, $i4 */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq -48(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -72(%rbp)
    /* $t8 = load %av0 */
    /* LoadInst */
    movq -72(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -80(%rbp)
    /* $t9 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -88(%rbp)
    /* $t10 = $i4 + $t9 */
    movq -48(%rbp), %r10
    addq -88(%rbp), %r10
    movq %r10, -96(%rbp)
    /* %av1 = addressAt a, $t10 */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq -96(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -104(%rbp)
    /* $t11 = load %av1 */
    /* LoadInst */
    movq -104(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -112(%rbp)
    /* $t12 = $t8 > $t11 */
    movq $0, %rax
    movq $1, %r10
    movq -80(%rbp), %r11
    cmp -112(%rbp) , %r11
    cmovg %r10, %rax
    movq %rax, -120(%rbp)
    /* jump $t12 */
    movq -120(%rbp), %r10
    cmp $1, %r10
    je L4
L6:
    /* $t21 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -128(%rbp)
    /* $t22 = $i4 + $t21 */
    movq -48(%rbp), %r10
    addq -128(%rbp), %r10
    movq %r10, -136(%rbp)
    /* $i4 = $t22 */
    /* CopyInst */
    movq -136(%rbp), %r10
    movq %r10, -48(%rbp)
    jmp L5
L4:
    /* %av2 = addressAt a, $i4 */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq -48(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -144(%rbp)
    /* $t14 = load %av2 */
    /* LoadInst */
    movq -144(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -152(%rbp)
    /* $tmp13 = $t14 */
    /* CopyInst */
    movq -152(%rbp), %r10
    movq %r10, -160(%rbp)
    /* $t15 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -168(%rbp)
    /* $t16 = $i4 + $t15 */
    movq -48(%rbp), %r10
    addq -168(%rbp), %r10
    movq %r10, -176(%rbp)
    /* %av3 = addressAt a, $t16 */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq -176(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -184(%rbp)
    /* $t17 = load %av3 */
    /* LoadInst */
    movq -184(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -192(%rbp)
    /* %av4 = addressAt a, $i4 */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq -48(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -200(%rbp)
    /* store $t17, %av4 */
    /* StoreInst */
    movq -192(%rbp), %r10
    movq -200(%rbp), %r11
    movq %r10, 0(%r11)
    /* $t18 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -208(%rbp)
    /* $t19 = $i4 + $t18 */
    movq -48(%rbp), %r10
    addq -208(%rbp), %r10
    movq %r10, -216(%rbp)
    /* %av5 = addressAt a, $t19 */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq -216(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -224(%rbp)
    /* store $tmp13, %av5 */
    /* StoreInst */
    movq -160(%rbp), %r10
    movq -224(%rbp), %r11
    movq %r10, 0(%r11)
    /* $t20 = true */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -232(%rbp)
    /* $swapped0 = $t20 */
    /* CopyInst */
    movq -232(%rbp), %r10
    movq %r10, -16(%rbp)
    jmp L6
L2:
    jmp L3
L1:
    leave
    ret
    .globl main
main:
    enter $(8 * 16), $0
    /* $t1 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -8(%rbp)
    /* $i0 = $t1 */
    /* CopyInst */
    movq -8(%rbp), %r10
    movq %r10, -16(%rbp)
L10:
    /* $t2 = 14 */
    /* CopyInst */
    movq $14, %r10
    movq %r10, -24(%rbp)
    /* $t3 = $i0 >= $t2 */
    movq $0, %rax
    movq $1, %r10
    movq -16(%rbp), %r11
    cmp -24(%rbp) , %r11
    cmovge %r10, %rax
    movq %rax, -32(%rbp)
    /* jump $t3 */
    movq -32(%rbp), %r10
    cmp $1, %r10
    je L7
    /* $t4 = 14 */
    /* CopyInst */
    movq $14, %r10
    movq %r10, -40(%rbp)
    /* $t5 = $t4 - $i0 */
    movq -40(%rbp), %r10
    subq -16(%rbp), %r10
    movq %r10, -48(%rbp)
    /* %av0 = addressAt a, $i0 */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq -16(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -56(%rbp)
    /* store $t5, %av0 */
    /* StoreInst */
    movq -48(%rbp), %r10
    movq -56(%rbp), %r11
    movq %r10, 0(%r11)
    /* $t6 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -64(%rbp)
    /* $t7 = $i0 + $t6 */
    movq -16(%rbp), %r10
    addq -64(%rbp), %r10
    movq %r10, -72(%rbp)
    /* $i0 = $t7 */
    /* CopyInst */
    movq -72(%rbp), %r10
    movq %r10, -16(%rbp)
    jmp L10
L7:
    call bubblesort
    /* $t8 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -80(%rbp)
    /* $i0 = $t8 */
    /* CopyInst */
    movq -80(%rbp), %r10
    movq %r10, -16(%rbp)
L9:
    /* $t9 = 14 */
    /* CopyInst */
    movq $14, %r10
    movq %r10, -88(%rbp)
    /* $t10 = $i0 >= $t9 */
    movq $0, %rax
    movq $1, %r10
    movq -16(%rbp), %r11
    cmp -88(%rbp) , %r11
    cmovge %r10, %rax
    movq %rax, -96(%rbp)
    /* jump $t10 */
    movq -96(%rbp), %r10
    cmp $1, %r10
    je L8
    /* %av1 = addressAt a, $i0 */
    /* AddressAt */
    movq a@GOTPCREL(%rip), %r11
    movq -16(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -104(%rbp)
    /* $t11 = load %av1 */
    /* LoadInst */
    movq -104(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -112(%rbp)
    movq -112(%rbp), %rdi
    call printInt
    call println
    /* $t12 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -120(%rbp)
    /* $t13 = $i0 + $t12 */
    movq -16(%rbp), %r10
    addq -120(%rbp), %r10
    movq %r10, -128(%rbp)
    /* $i0 = $t13 */
    /* CopyInst */
    movq -128(%rbp), %r10
    movq %r10, -16(%rbp)
    jmp L9
L8:
    leave
    ret
