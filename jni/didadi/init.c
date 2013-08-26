#include <string.h>

#include "didadi.h"


static char INIT_SUCCESS_MAGIC[] = "听天空在哭泣";


void didadi_init(char *result_buf, size_t buf_size)
{
   memset(result_buf, 0, buf_size);
   memcpy(result_buf, INIT_SUCCESS_MAGIC, 19);
}


/* vim:set ai et ts=4 sw=4 sts=4 fenc=utf-8: */
