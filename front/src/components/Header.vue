<template>
  <el-header class="header">
    <!--      <nav>-->
    <!--        <RouterLink to="/">Home</RouterLink>-->
    <!--        <RouterLink to="/write">글 작성</RouterLink>-->
    <!--      </nav>-->
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/write">글 작성</el-menu-item>
    </el-menu>
  </el-header>
</template>

<script setup lang="ts">

</script>

<style scoped>
.header {
  padding : 0;
  height: 60px;
}
</style>