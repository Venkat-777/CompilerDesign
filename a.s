    .globl foo
foo:
    enter $(8 * 3), $0
    $t3
    movq %rdi, -8(%rbp)
    .globl main
main:
    enter $(8 * 0), $0
