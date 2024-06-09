    .comm a, 112, 8
    .globl bubblesort
bubblesort:
    enter $(8 * 30), $0
    /* CopyInst */
    movq $1, %r10
    movq %r10, -8(%rbp)
    /* CopyInst */
    movq -8(%rbp), %r10
    movq %r10, -16(%rbp)
L3:
    /* $t2 = not $swapped0 */
    movq -16(%rbp), %r10
    movq $1, %r11
    subq %r10, %r11
    movq %r11, -24(%rbp)
    movq -24(%rbp), %r10
    cmp $1, %r10
    je L1
    /* CopyInst */
    movq $0, %r10
    movq %r10, -32(%rbp)
    /* CopyInst */
    movq -32(%rbp), %r10
    movq %r10, -16(%rbp)
    /* CopyInst */
    movq $0, %r10
    movq %r10, -40(%rbp)
    /* CopyInst */
    movq -40(%rbp), %r10
    movq %r10, -48(%rbp)
L5:
    /* CopyInst */
    movq $13, %r10
    movq %r10, -56(%rbp)
    movq $0, %rax
    movq $1, %r10
    movq -48(%rbp), %r11
    cmp -56(%rbp) , %r11
    cmovge %r10, %rax
    movq %rax, -64(%rbp)
    movq -64(%rbp), %r10
    cmp $1, %r10
    je L2
    movq a@GOTPCREL(%rip), %r11
    movq -48(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -72(%rbp)
    movq -72(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -80(%rbp)
    /* CopyInst */
    movq $1, %r10
    movq %r10, -88(%rbp)
    movq -48(%rbp), %r10
    addq -88(%rbp), %r10
    movq %r10, -96(%rbp)
    movq a@GOTPCREL(%rip), %r11
    movq -96(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -104(%rbp)
    movq -104(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -112(%rbp)
    movq $0, %rax
    movq $1, %r10
    movq -80(%rbp), %r11
    cmp -112(%rbp) , %r11
    cmovg %r10, %rax
    movq %rax, -120(%rbp)
    movq -120(%rbp), %r10
    cmp $1, %r10
    je L4
L6:
    /* CopyInst */
    movq $1, %r10
    movq %r10, -128(%rbp)
    movq -48(%rbp), %r10
    addq -128(%rbp), %r10
    movq %r10, -136(%rbp)
    /* CopyInst */
    movq -136(%rbp), %r10
    movq %r10, -48(%rbp)
    jmp L5
L4:
    movq a@GOTPCREL(%rip), %r11
    movq -48(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -144(%rbp)
    movq -144(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -152(%rbp)
    /* CopyInst */
    movq -152(%rbp), %r10
    movq %r10, -160(%rbp)
    movq a@GOTPCREL(%rip), %r11
    movq -48(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -168(%rbp)
    /* CopyInst */
    movq $1, %r10
    movq %r10, -176(%rbp)
    movq -48(%rbp), %r10
    addq -176(%rbp), %r10
    movq %r10, -184(%rbp)
    movq a@GOTPCREL(%rip), %r11
    movq -184(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -192(%rbp)
    movq -192(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -200(%rbp)
    /* StoreInst */
    movq -200(%rbp), %r10
    movq -168(%rbp), %r11
    movq %r10, 0(%r11)
    /* CopyInst */
    movq $1, %r10
    movq %r10, -208(%rbp)
    movq -48(%rbp), %r10
    addq -208(%rbp), %r10
    movq %r10, -216(%rbp)
    movq a@GOTPCREL(%rip), %r11
    movq -216(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -224(%rbp)
    /* StoreInst */
    movq -160(%rbp), %r10
    movq -224(%rbp), %r11
    movq %r10, 0(%r11)
    /* CopyInst */
    movq $1, %r10
    movq %r10, -232(%rbp)
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
    /* CopyInst */
    movq $0, %r10
    movq %r10, -8(%rbp)
    /* CopyInst */
    movq -8(%rbp), %r10
    movq %r10, -16(%rbp)
L10:
    /* CopyInst */
    movq $14, %r10
    movq %r10, -24(%rbp)
    movq $0, %rax
    movq $1, %r10
    movq -16(%rbp), %r11
    cmp -24(%rbp) , %r11
    cmovge %r10, %rax
    movq %rax, -32(%rbp)
    movq -32(%rbp), %r10
    cmp $1, %r10
    je L7
    movq a@GOTPCREL(%rip), %r11
    movq -16(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -40(%rbp)
    /* CopyInst */
    movq $14, %r10
    movq %r10, -48(%rbp)
    movq -48(%rbp), %r10
    subq -16(%rbp), %r10
    movq %r10, -56(%rbp)
    /* StoreInst */
    movq -56(%rbp), %r10
    movq -40(%rbp), %r11
    movq %r10, 0(%r11)
    /* CopyInst */
    movq $1, %r10
    movq %r10, -64(%rbp)
    movq -16(%rbp), %r10
    addq -64(%rbp), %r10
    movq %r10, -72(%rbp)
    /* CopyInst */
    movq -72(%rbp), %r10
    movq %r10, -16(%rbp)
    jmp L10
L7:
    call bubblesort
    /* CopyInst */
    movq $0, %r10
    movq %r10, -80(%rbp)
    /* CopyInst */
    movq -80(%rbp), %r10
    movq %r10, -16(%rbp)
L9:
    /* CopyInst */
    movq $14, %r10
    movq %r10, -88(%rbp)
    movq $0, %rax
    movq $1, %r10
    movq -16(%rbp), %r11
    cmp -88(%rbp) , %r11
    cmovge %r10, %rax
    movq %rax, -96(%rbp)
    movq -96(%rbp), %r10
    cmp $1, %r10
    je L8
    movq a@GOTPCREL(%rip), %r11
    movq -16(%rbp), %r10
    imulq $8, %r10
    addq %r10, %r11
    movq %r11, -104(%rbp)
    movq -104(%rbp), %r10
    movq 0(%r10), %r11
    movq %r11, -112(%rbp)
    movq -112(%rbp), %rdi
    call printInt
    call println
    /* CopyInst */
    movq $1, %r10
    movq %r10, -120(%rbp)
    movq -16(%rbp), %r10
    addq -120(%rbp), %r10
    movq %r10, -128(%rbp)
    /* CopyInst */
    movq -128(%rbp), %r10
    movq %r10, -16(%rbp)
    jmp L9
L8:
    leave
    ret
