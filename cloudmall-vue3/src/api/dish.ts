import request from '@/utils/request' // 引入自定义的axios函数

/**
 * 添加商品
 * @param params 添加商品的DTO对象
 * @returns
 */
export const addDishAPI = (params: any) => {
  return request({
    url: '/dish',
    method: 'post',
    data: { ...params }
  })
}

/**
 * 获取商品分页列表
 * @param params pageData
 * @returns
 */
export const getDishPageListAPI = (params: any) => {
  console.log('dish-params', params)
  return request({
    url: '/dish/page',
    method: 'get',
    params
  })
}

/**
 * 根据id获取商品信息，用于回显
 * @param id 商品id
 * @returns
 */
export const getDishByIdAPI = (id: number) => {
  return request({
    url: `/dish/${id}`,
    method: 'get'
  })
}

/**
 * 修改商品信息
 * @param params 更新商品信息的DTO对象
 * @returns
 */
export const updateDishAPI = (params: any) => {
  return request({
    url: '/dish',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 修改商品状态
 * @param params 商品id
 * @returns
 */
export const updateDishStatusAPI = (id: number) => {
  console.log('发请求啊！', id)
  return request({
    url: `/dish/status/${id}`,
    method: 'put'
  })
}

/**
 * 根据ids批量删除商品
 * @param ids 商品ids
 * @returns
 */
export const deleteDishesAPI = (ids: string) => {
  return request({
    url: '/dish',
    method: 'delete',
    params: { ids }
  })
}

