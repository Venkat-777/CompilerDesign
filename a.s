    .globl rec
rec:
    enter $(8 * 2), $0
    movq %rdi, -8(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($a0) */
    movq -8(%rbp), %rdi
    call printInt
    movq %rax, -16(%rbp)
    leave
    ret
    .globl main
main:
    enter $(8 * 2), $0
    /* $t0 = 41 */
    /* CopyInst */
    movq $41, %r10
    movq %r10, -24(%rbp)
    /* call Symbol(rec:func(TypeList(int)):void) ($t0) */
    movq -24(%rbp), %rdi
    call rec
    movq %rax, -16(%rbp)
    leave
    ret
