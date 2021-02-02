
/**
 * 无权限，默认渲染的路由
 */
export const constantRoutes = [
  {
    path: '/upload',
    name: 'upload',
    component: () => import('@/views/upload/upload'),
    hidden: true
  },
  {
    path: '/demo',
    name: 'demo',
    component: () => import('@/views/demo/demo'),
    hidden: true
  }
]

/**
* 需要根据用户角色动态加载的路由
*/
export const asyncRoutes = [
]

// 所有上面定义的路由都要写在下面的routers里
export let constRouters = [
  asyncRoutes,
  constantRoutes
]
