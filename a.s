    .globl main
main:
    enter $(8 * 4), $0
    /* $t0 = 7 */
    /* CopyInst */
    movq $7, %r10
    movq %r10, -8(%rbp)
    /* call Symbol(printInt:func(TypeList(int)):void) ($t0) */
    movq -8(%rbp), %rdi
    call printInt
    /* $t1 = true */
    /* CopyInst */
    movq $1, %r10
    movq %r10, -16(%rbp)
    /* call Symbol(printBool:func(TypeList(bool)):void) ($t1) */
    movq -16(%rbp), %rdi
    call printBool
    /* call Symbol(println:func(TypeList()):void) () */
    call println
    /* $t2 = call Symbol(readInt:func(TypeList()):int) () */
    call readInt
    leave
    ret
