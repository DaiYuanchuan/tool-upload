import Vue from 'vue'
import Router from 'vue-router'
import { constantRoutes } from '../router/routers'

Vue.use(Router)

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter () {
  const newRouter = createRouter()
  // 重置路由
  router.matcher = newRouter.matcher
}

export default router
