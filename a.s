    .globl test
test:
    enter $(8 * 8), $0
    movq %rdi, -8(%rbp)
    movq %rsi, -16(%rbp)
    /* $t2 = $a0 + $b1 */
    movq -8(%rbp), %r10
    addq -16(%rbp), %r10
    movq %r10, -24(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t2) */
    movq -24(%rbp), %rdi
    call printInt
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    /* $t3 = $a0 - $b1 */
    movq -8(%rbp), %r10
    subq -16(%rbp), %r10
    movq %r10, -32(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t3) */
    movq -32(%rbp), %rdi
    call printInt
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    /* $t4 = $a0 * $b1 */
    movq -8(%rbp), %r10
    imul -16(%rbp), %r10
    movq %r10, -40(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t4) */
    movq -40(%rbp), %rdi
    call printInt
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    /* $t5 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -48(%rbp)
    /* $t6 = $b1 != $t5 */
    movq $0, %rax
    movq $1, %r10
    movq -16(%rbp), %r11
    cmp -48(%rbp) , %r11
    cmovne %r10, %rax
    movq %rax, -56(%rbp)
    /* jump $t6 */
    movq -56(%rbp), %r10
    cmp $1, %r10
    je L1
    /* call Symbol(println:func(TypeList()):void) () */
    call println
L2:
    leave
    ret
L1:
    /* $t7 = $a0 / $b1 */
    movq -8(%rbp), %rax
    cqto
    idivq -16(%rbp)
    movq %rax, -64(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t7) */
    movq -64(%rbp), %rdi
    call printInt
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    jmp L2
    .globl main
main:
    enter $(8 * 12), $0
    /* $t2 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -8(%rbp)
    /* $i0 = $t2 */
    /* CopyInst */
    movq -8(%rbp), %r10
    movq %r10, -16(%rbp)
L5:
    /* $t3 = 10 */
    /* CopyInst */
    movq $10, %r10
    movq %r10, -24(%rbp)
    /* $t4 = $i0 > $t3 */
    movq $0, %rax
    movq $1, %r10
    movq -16(%rbp), %r11
    cmp -24(%rbp) , %r11
    cmovg %r10, %rax
    movq %rax, -32(%rbp)
    /* jump $t4 */
    movq -32(%rbp), %r10
    cmp $1, %r10
    je L3
    /* $t5 = 0 */
    /* CopyInst */
    movq $0, %r10
    movq %r10, -40(%rbp)
    /* $j1 = $t5 */
    /* CopyInst */
    movq -40(%rbp), %r10
    movq %r10, -48(%rbp)
L6:
    /* $t6 = 10 */
    /* CopyInst */
    movq $10, %r10
    movq %r10, -56(%rbp)
    /* $t7 = $j1 > $t6 */
    movq $0, %rax
    movq $1, %r10
    movq -48(%rbp), %r11
    cmp -56(%rbp) , %r11
    cmovg %r10, %rax
    movq %rax, -64(%rbp)
    /* jump $t7 */
    movq -64(%rbp), %r10
    cmp $1, %r10
    je L4
    /* call Symbol(test:func(TypeList(int, int)):void) ($i0$j1) */
    movq -48(%rbp), %rsi
    movq -16(%rbp), %rdi
    call test
    /* $t8 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -72(%rbp)
    /* $t9 = $j1 + $t8 */
    movq -48(%rbp), %r10
    addq -72(%rbp), %r10
    movq %r10, -80(%rbp)
    /* $j1 = $t9 */
    /* CopyInst */
    movq -80(%rbp), %r10
    movq %r10, -48(%rbp)
    jmp L6
L4:
    /* $t10 = 1 */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -88(%rbp)
    /* $t11 = $i0 + $t10 */
    movq -16(%rbp), %r10
    addq -88(%rbp), %r10
    movq %r10, -96(%rbp)
    /* $i0 = $t11 */
    /* CopyInst */
    movq -96(%rbp), %r10
    movq %r10, -16(%rbp)
    jmp L5
L3:
    leave
    ret
